function Animation(animationId, name, numDirections, direction) {
    this.name = name;

    this._wait = 0;
    this._frame = 0;
    this.offsetX = 0;
    this.offsetY = 0;
    this.numDirections = numDirections;
    this.direction = direction;
    this.actions = animations[animationId][name];
    this.labels = {};
    for (var i = 0; i < this.actions.length; i++) {
        var ss = this.actions[i].split(" ");
        if (ss[0] == 'label') this.labels[ss[1]] = i;
    }
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

    return new Animation(unit.type.Animations, capitalize(context.action), unit.type.Building ? 1 : 9, direction);
};

Animation.prototype.animate = function() {
    var animationBefore = jQuery.extend({}, this);
    if (this._wait == 0) {
        if (this.name == 'Still') this.currentActionIndex = this.currentActionIndex % this.actions.length;
        if (this.currentActionIndex < this.actions.length) {
            var ss = this.actions[this.currentActionIndex].split(' ');
            var actionName = this[ss[0]];
            if (actionName) {
                actionName.apply(this, ss.slice(1));
            } else {
                console.log('animation action ' + ss[0] + ' not found');
            }
        }

        this.currentActionIndex++;

        var diff = {};
        var props = ['_frame', 'direction', 'offsetX', 'offsetY', '_wait'];

        for (var i = 0; i < props.length; i++) {
            if (this[props[i]] != animationBefore[props[i]]) {
                diff[props[i]] = true;
            }
        }

        return diff;
    } else {
        this._wait--;
        return {'_wait': true};
    }
};

// Actions

Animation.prototype.frame = function(frameNo) {
    this._frame = parseInt(frameNo) / 5;
};

Animation.prototype.wait = function(ticks) {
    this._wait = parseInt(ticks);
    // todo haste and slow
};

Animation.prototype["random-goto"] = function(chance, label) {
    var _chance = parseInt(chance);
    if (Math.random() * 100 < _chance) {
        this.goto(label);
    }
};

Animation.prototype.goto = function(label) {
    if (this.labels[label] === undefined) console.log('label ' + label + ' not found');
    else {
        this.currentActionIndex = this.labels[label] + 1;
    }
};

Animation.prototype["random-rotate"] = function(framesToRotate) {
    if (Math.random() > 0.5) {
        this.rotate(framesToRotate);
    } else {
        this.rotate(framesToRotate);
    }
};

/**
 * @param framesToRotate Number of frames to rotate (>0 clockwise, <0 counterclockwise)
 */
Animation.prototype.rotate = function(framesToRotate) {
    var _framesToRotate = parseInt(framesToRotate);
    this.direction = (this.direction + (_framesToRotate % this.numDirections) + this.numDirections)
        % this.numDirections;
};

Animation.prototype.move = function(offset) {
    var _offset = parseInt(offset);
    this.offsetX += _offset * Directions[this.direction][0];
    this.offsetY += _offset * Directions[this.direction][1];
};

Animation.prototype.label = function() { /* empty stub */ };
Animation.prototype.unbreakable = function() { /* empty stub */ };