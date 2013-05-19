var Animation = {
    frame: function(animation, args, scale) {
        animation.Frame = parseInt(args) / 5;
    },
    wait: function(animation, args, scale) {
        animation.Wait = parseInt(args);
        // todo haste and slow
    },
    "random-goto": function(animation, args, scale) {
        var chance = parseInt(args[0]);
        if (Math.random() * 100 < chance) {
            this.goto(animation, new Array(args[1]), scale);
        }
    },
    goto: function(animation, args, scale) {
        for (var i = 0; i < animation.Action.length; i++) {
            if (animation.Action[i] == 'label ' + args[0]) {
                animation.ActionIndex = (i + 1) % animation.Action.length;
                return;
            }
        }
    },
    "random-rotate": function(animation, args, scale) {
        if (Math.random() > 0.5) {
            this.rotate(animation, new Array(args[0]));
        } else {
            this.rotate(animation, new Array(-parseInt(args[0]) + ""));
        }
    },
    /**
     * @param animation Animation object
     * @param args Number of frames to rotate (>0 clockwise, <0 counterclockwise)
     */
    rotate : function(animation, args) {
        animation.Direction = (animation.Direction + (parseInt(args[0]) % animation.NumDirections) + animation.NumDirections)
            % animation.NumDirections;
    },
    move: function(animation, args, scale) {
        return parseInt(args[0]);
    }
};