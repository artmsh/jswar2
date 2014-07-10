function Missile(x, y, missileType, layoutManager) {
    this.x = x;
    this.y = y;
    this.type = missiles[missileType];
    this.image = '/assets/images/' + this.type.File;

    this.canvas = $("<canvas class='missile'></canvas>");
    this.canvas.attr({ "width" : this.type.Size[0], "height" : this.type.Size[1] });
    this.canvas.css({ "z-index" : this.type.DrawLevel });
    this.context = this.canvas[0].getContext('2d');

    // todo different missile classes, not only "missile-class-cycle-once"

    $(parentEl).append(this.canvas);

    this.framesLeft = this.type.Frames;
    this.sleepLeft = this.type.Sleep;
}

Missile.prototype.redrawIfNeeded = function() {
    var x = this.x - Math.round(this.type.Size[0] / 2);
    var y = this.y - Math.round(this.type.Size[1] / 2);

    this.canvas.css({"margin-top": y, "margin-left": x});
    this.context.clearRect(0, 0, this.type.Size[0], this.type.Size[1]);

    var image = ResourcePreloader.get(this.image);
    this.context.drawImage(image, 0,
        (this.framesLeft - 1) * this.type.Size[1], this.type.Size[0], this.type.Size[1], 0, 0,
        this.type.Size[0], this.type.Size[1]);

    if (this.sleepLeft > 0) {
        this.sleepLeft--;
    } else {
        this.framesLeft--;
        this.sleepLeft = this.type.Sleep;
    }
};

Missile.prototype.detach = function() {
    this.canvas.detach();
};

Missile.prototype.isDone = function() {
    return this.framesLeft <= 0;
};