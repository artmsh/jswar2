function Animation(animationId, name, numDirections, direction) {
    this.name = name;

    this.unbreakableModeOn = false;
    this.waiting = false;
    this._wait = 0;
    this._frame = 0;
    this.offsetX = 0;
    this.offsetY = 0;
    this.numDirections = numDirections;
    this.direction = direction;
    var anims = animations[animationId][name];
    this.actions = anims.map(function(action) {
        var ss = action.split(' ');
        var actionName = this[ss[0]];

        var fn;
        if (actionName) {
            fn = actionName;
        } else {

            // fixme temporary fallback for missing animations
            fn = function() {
                console.log('animation action ' + ss[0] + ' not found');
                return this.next();
            }
        }

        return { fn: fn, params: ss.slice(1) };
    }, this);
    this.labels = {};
    anims.forEach(function(animation, index) {
        var ss = animation.split(" ");
        if (ss[0] == 'label') this.labels[ss[1]] = index;
    }, this);
    this.currentActionIndex = 0;
}

Animation.directionsMap = [3, 4, 5, 0, 2, 0, 6, 0, 1, 0, 7];
function index(dx, dy) { return (dx + 1) + 4 * (dy + 1); }
// x1-x2 == 0, y1-y2 == 1    1 + 4 * 2
// x1-x2 == -1 y1-y2 == 1    0 + 4 * 2
// x1-x2 == -1 y1-y2 == 0    0 + 4 * 1
// x1-x2 == -1 y1-y2 ==-1    0 + 4 * 0
// x1-x2 ==  0 y1-y2 ==-1    1 + 4 * 0
// x1-x2 ==  1 y1-y2 ==-1    2 + 4 * 0
// x1-x2 ==  1 y1-y2 == 0    2 + 4 * 1
// x1-x2 ==  1 y1-y2 == 1    2 + 4 * 2

Animation.buildFrom = function(context, unit) {
    var direction = 0;
    if (context.action === 'move') {
        direction = Animation.directionsMap[index(unit.x - context.moveX, unit.y - context.moveY)];
    }

    return new Animation(unit.uiType.Animations, capitalize(context.action), unit.isBuilding() ? 1 : 9, direction);
};

Animation.prototype.animate = function() {
    var animationBefore = jQuery.extend({}, this);

    var action = this.actions[this.currentActionIndex];
    while (action.fn.apply(this, action.params)) {
        action = this.actions[this.currentActionIndex];
    }

    var diff = {};
    var props = ['_frame', 'direction', 'offsetX', 'offsetY', '_wait', 'unbreakableModeOn'];

    for (var i = 0; i < props.length; i++) {
        if (this[props[i]] != animationBefore[props[i]]) {
            diff[props[i]] = true;
        }
    }

    return diff;
};

Animation.prototype.next = function() {
    if (this.currentActionIndex < this.actions.length - 1) {
        this.currentActionIndex++;

        return true;
    } else {
        return false;
    }
};

// Actions
// should return boolean

Animation.prototype.frame = function(frameNo) {
    this._frame = parseInt(frameNo) / 5;
    return this.next();
};

Animation.prototype.wait = function(ticks) {
    if (!this.waiting) {
        this._wait = parseInt(ticks);
        this.waiting = true;

        return false;
    } else {
        if (this._wait == 0) {
            this.waiting = false;
            return this.next();
        } else {
            this._wait--;
            return false;
        }
    }
    // todo haste and slow
};

Animation.prototype["random-goto"] = function(chance, label) {
    var _chance = parseInt(chance);
    if (Math.random() * 100 < _chance) {
        return this.goto(label);
    } else {
        return this.next();
    }
};

Animation.prototype.goto = function(label) {
    if (this.labels[label] === undefined) {
        console.warn('label ' + label + ' not found');
        return this.next();
    } else {
        this.currentActionIndex = this.labels[label] + 1;
        return true;
    }
};

Animation.prototype["random-rotate"] = function(framesToRotate) {
    if (Math.random() > 0.5) {
        return this.rotate(framesToRotate);
    } else {
        return this.rotate(-framesToRotate);
    }
};

/**
 * @param framesToRotate Number of frames to rotate (>0 clockwise, <0 counterclockwise)
 */
Animation.prototype.rotate = function(framesToRotate) {
    var _framesToRotate = parseInt(framesToRotate);
    this.direction = (this.direction + (_framesToRotate % this.numDirections) + this.numDirections)
        % this.numDirections;
    return this.next();
};

Animation.prototype.move = function(offset) {
    var _offset = parseInt(offset);
    this.offsetX += _offset * Directions[this.direction][0];
    this.offsetY += _offset * Directions[this.direction][1];
    return this.next();
};

Animation.prototype.label = function() {
    return this.next();
};

Animation.prototype.unbreakable = function(target) {
    if (target == 'begin') {
        this.unbreakableModeOn = true;
    } else if (target == 'end') {
        this.unbreakableModeOn = false;
    }
    return this.next();
};