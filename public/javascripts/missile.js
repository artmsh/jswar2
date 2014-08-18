function Missile(x, y, missileType, layoutManager) {
    this.x = x;
    this.y = y;
    this.type = missiles[missileType];
    this.image = '/assets/images/' + this.type.File;

    this.layout = layoutManager.createLayout(this.type.Size[0], this.type.Size[1], '', 'missile', this.type.DrawLevel);

    // todo different missile classes, not only "missile-class-cycle-once"

    this.framesLeft = this.type.Frames;
    this.sleepLeft = this.type.Sleep;
}

Missile.prototype.redrawIfNeeded = function() {
    var x = this.x - Math.round(this.type.Size[0] / 2);
    var y = this.y - Math.round(this.type.Size[1] / 2);

    this.layout.canvasEl.css({"margin-top": y, "margin-left": x});
    this.layout.context.clearRect(0, 0, this.type.Size[0], this.type.Size[1]);

    var image = ResourcePreloader.get(this.image);
    this.layout.context.drawImage(image, 0,
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
    this.layout.canvasEl.detach();
};

Missile.prototype.isDone = function() {
    return this.framesLeft <= 0;
};