var fonts = [];
fonts['game'] = {src: 'assets/images/ui/fonts/game.png', width: 13, height: 14, actualWidth: 13 - 4};
fonts['large'] = {src: 'assets/images/ui/fonts/large.png', width: 17, height: 17, actualWidth: 17 - 7};
fonts['small'] = {src: 'assets/images/ui/fonts/small.png', width: 7, height: 6, actualWidth: 7 - 2};

var buttonBackgrounds = [];
buttonBackgrounds['orc-large-normal'] = {
    normal : 'assets/images/ui/orc/widgets/button-large-normal.png',
    grayed : 'assets/images/ui/orc/widgets/button-large-grayed.png',
    pressed : 'assets/images/ui/orc/widgets/button-large-pressed.png'
};
buttonBackgrounds['orc-thin-medium'] = buildBackground('orc', 'button-thin-medium');
buttonBackgrounds['down-arrow'] = buildBackground('orc', 'down-arrow');

function buildBackground(race, widget) {
    var path = 'assets/images/ui/' + race + '/widgets/';
    var o = {};
    ['normal', 'grayed', 'pressed'].forEach(function(e) {
        o[e] = path + widget + '-' + e + '.png';
    });

    return o;
}

function CanvasLabel(x, y, text, font) {
    this.zindex = 1;
    this.x = x;
    this.y = y;
    this.text = text;
    this.font = font;
    this.width = font.actualWidth * text.length;
    this.height = font.height;
}

CanvasLabel.prototype.draw = function(context) {
    var image = new Image();
    image.src = this.font.src;

    for (var i = 0 ; i < this.text.length ; i++) {
        var c = this.text.charCodeAt(i) - 32;
        context.drawImage(image, (c % 15) * this.font.width, Math.floor(c / 15) * this.font.height, this.font.width, this.font.height,
            this.x + i * this.font.actualWidth, this.y, this.font.width, this.font.height);
    }
};

function CanvasButton(caption, x, y, width, height, color, background, action) {
    this.zindex = 0;
    this.caption = caption;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.actualHeight = height;
    this.color = color;
    this.background = background;
    this.disabled = false;
    this.pressed = false;
    this.action = action;
}

CanvasButton.prototype.draw = function(context) {
    if (this.background) {
        var image = new Image();
        if (this.disabled) {
            image.src = this.background.grayed;
        } else if (this.pressed) {
            image.src = this.background.pressed;
        } else {
            image.src = this.background.normal;
        }
        context.drawImage(image, this.x, this.y);
    } else if (this.color) {
        context.save();
        context.fillStyle = this.disabled ? this.color.grayed : this.color.normal;
        context.fillRect(this.x, this.y, this.x + this.width, this.y + this.height);
        context.restore();
    }

    var label = new CanvasLabel(0, 0, this.caption, fonts.game);
    label.x = this.x + (this.width - label.width) / 2;
    label.y = this.y + (this.height - label.height) / 2;
    label.draw(context);
};

function CanvasContainer(canvasEl) {
    this.context = canvasEl.getContext('2d');
    this.components = [];
    this.width = canvasEl.width;
    this.height = canvasEl.height;

    var that = this;
    $(canvasEl).click(function(e) {
        var mouseX = e.pageX - this.offsetLeft;
        var mouseY = e.pageY - this.offsetTop;

        that.components.sort(function(c1,c2) { return c2.zindex - c1.zindex; });
        for (var c in that.components) {
            var component = that.components[c];
            if (component.x <= mouseX && component.x + component.width >= mouseX && component.y <= mouseY && component.y + component.actualHeight >= mouseY) {
                if (component.click) {
                    component.click.call(component, {x : mouseX - component.x, y : mouseY - component.y});
                }
                component.pressed = !component.pressed;
                if (component.action) {
                    setTimeout(component.action, 500);
                }
                break;
            } else {
                component.pressed = false;
            }
        }

        that.draw();
    });
}

CanvasContainer.prototype.addComponent = function(component) {
    this.components.push(component);
    return component;
};

CanvasContainer.prototype.clear = function() {
    this.components = [];
    this.context.clearRect(0, 0, this.width, this.height);
};

CanvasContainer.prototype.draw = function() {
    this.context.clearRect(0, 0, this.width, this.height);

    var components = this.components;
    components.sort(function(c1,c2) { return c1.zindex - c2.zindex; });
    for (var component in components) {
        components[component].draw(this.context);
    }
};

function CanvasComboBox(values, x, y, width, height) {
    this.zindex = 2;
    this.values = values;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.actualHeight = height;
    this.selected = (values && values.length > 0) ? values[0] : null;
    this.disabled = false;
    this.pressed = false;
    this.click = function(e) {
        if (this.pressed) {
            this.zindex--;
            this.actualHeight = this.height;
            var selectedIndex = Math.floor(e.y / this.height);
            if (selectedIndex > 0 && selectedIndex <= this.values.length) {
                this.selected = this.values[selectedIndex - 1];
            }
        } else {
            this.actualHeight = (this.values.length + 1) * this.height;
            this.zindex++;
        }
    }
}

CanvasComboBox.prototype.draw = function(context) {
    var leftImage = new Image();
    var bl = buttonBackgrounds['orc-thin-medium'];
    if (this.disabled) {
        leftImage.src = bl.grayed;
    } else {
        leftImage.src = bl.normal;
    }

    context.drawImage(leftImage, this.x, this.y, this.width, this.height);

    if (!this.pressed) {
        var br = buttonBackgrounds['down-arrow'];
        var rightImage = new Image();
        rightImage.src = br.normal;
        context.drawImage(rightImage, this.x + this.width - rightImage.width, this.y);
    } else {
        for (var i = 0; i < this.values.length; i++) {
            var image = new Image();
            image.src = bl.normal;
            var y = this.y + this.height * (i + 1);
            context.drawImage(image, this.x, y, this.width, this.height);

            var label1 = new CanvasLabel(this.x + 3, y + 4, this.values[i], fonts.game);
            label1.draw(context);
        }
    }

    var label = new CanvasLabel(this.x + 3, this.y + 4, this.selected, fonts.game);
    label.draw(context);
};