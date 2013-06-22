function InfoPanel(selection, tileset) {
    this.canvas = $('#infopanel')[0];
    this.context = this.canvas.getContext('2d');
    this.selection = selection;
//    this.race = race.toLowerCase();
    this.tileset = tileset.toLowerCase();
    this.level = 0;

    this.isDrawNeeded = true;

    ResourcePreloader.add('assets/images/ui/orc/infopanel.png');
    ResourcePreloader.add('assets/images/ui/human/infopanel.png');

    ResourcePreloader.add('assets/images/tilesets/' + this.tileset + '/icons.png');
}

InfoPanel.prototype.onSelectionChanged = function() {
    this.isDrawNeeded = true;
};

InfoPanel.prototype.drawBox = function(x, y, w, h, image, dx, dy) {
    this.context.strokeStyle = "black";
    this.context.lineWidth = 2;
    this.context.strokeRect(x, y, w - 2, h - 2);

    this.context.beginPath();
    this.context.strokeStyle = "white";
    this.context.moveTo(x + 3, y + 2);
    this.context.lineTo(x + 3, y + h - 3);

    this.context.moveTo(x + 2, y + 3);
    this.context.lineTo(x + w - 3, y + 3);
    this.context.stroke();

    this.context.beginPath();
    this.context.strokeStyle = "gray";
    this.context.moveTo(x + w - 4, y + 4);
    this.context.lineTo(x + w - 4, y + h - 3);
//
    this.context.moveTo(x + 4, y + h - 4);
    this.context.lineTo(x + w - 4, y + h - 4);
    this.context.stroke();

    this.context.drawImage(image, dx, dy, w - 8, h - 8, x + 3, y + 3, w - 8, h - 8);
};

InfoPanel.prototype.drawLifeBar = function(unit, pos, vars) {
    this.context.fillStyle = "black";
    this.context.fillRect(pos[0], pos[1], 54, 7);

    if (unit.vars[vars.Variable1] && unit.vars[vars.Variable2]) {
        var hpPercentage = ~~((unit.vars[vars.Variable1] * 100) / unit.vars[vars.Variable2]);

        if (hpPercentage > 75) {
            this.context.fillStyle = "darkgreen";
        } else if (hpPercentage > 50) {
            this.context.fillStyle = "yellow";
        } else if (hpPercentage > 25) {
            this.context.fillStyle = "orange";
        } else {
            this.context.fillStyle = "red";
        }

        this.context.fillRect(pos[0] + 2, pos[1], ~~((hpPercentage * (54 - 4)) / 100), 5);
    }
};

InfoPanel.prototype.prepareFormatting = function(vars) {
    this.context.font = vars.Font ? fonts[vars.Font] : fonts[this.defaultFont];

    if (vars.Centered) {
        this.context.textAlign = "center";
    } else if (vars.TextAlign) {
        this.context.textAlign = vars.TextAlign;
    } else {
        this.context.textAlign = "left";
    }

    this.context.fillStyle = "yellow";
};

InfoPanel.checkVars = function(unit, v, field) {
    var result = false;
    if (unit[v] === undefined) console.log("Unknown variable " + v);
    else if (unit[v][field] === undefined) console.log("Unknown field "+ field + " on variable " + v);
    else result = true;

    return result;
};

InfoPanel.prototype.drawFormattedText2 = function(unit, pos, vars) {
    this.prepareFormatting(vars);

    var var1, var2;

    if (vars.Variable) var1 = var2 = vars.Variable;
    if (vars.Variable1) var1 = vars.Variable1;
    if (vars.Variable2) var2 = vars.Variable2;

    this.context.fillText(sprintf(vars.Format, unit.vars[var1], unit.vars[var2]), pos[0], pos[1]);
};

InfoPanel.prototype.drawText = function(unit, pos, vars) {
    this.prepareFormatting(vars);

    if (vars.ShowName) {
        this.context.fillText(unit.type.Name, pos[0], pos[1]);
    } else {
        var text = "";
        if (vars.Text) {
            if (vars.Text.constructor == Function) {
                text = vars.Text.call(this, unit);
            } else if (vars.Text.constructor == String) {
                text = vars.Text;
            }
        }

        if (vars.Variable) {
            if (!vars.Stat) {
                // careful if vars.Variable is undefined, current scenario don't guarantee it
                text += unit.vars[vars.Variable];
            } else if (vars.Stat) {
                var val = unit.type[vars.Variable];
                var diff = unit.vars[vars.Variable] - val;
                if (diff == 0) {
                    text += val;
                } else {
                    text += sprintf(diff > 0 ? "%d~<+%d~>" : "%d~<-%d~>", val, diff);
                }
            }
        }

        this.context.fillText(text, pos[0], pos[1]);
    }
};

InfoPanel.prototype.drawFormattedText = function(unit, pos, vars) {
    this.prepareFormatting(vars);

    this.context.fillText(sprintf(vars.Format, unit.vars[vars.Variable]), pos[0], pos[1]);
};

InfoPanel.prototype.drawIcon = function(unit, pos, vars) {
    if (vars.Unit !== undefined) {
        var u = unit;
        if (vars.Unit == 'Inside') {
            u = unit.UnitInside;
        } else if (vars.Unit == 'Container') {
            u = unit.Container;
        } else if (vars.Unit == 'Worker') {
            if (unit.getCurrentAction() == UnitAction.Built) {
                u = unit.getCurrentOrder().worker;
            }
        } else if (vars.Unit == 'Goal') {
            u = unit.Goal;
        }

        var iconNum = icons[u.type.Icon];
        this.context.drawImage(ResourcePreloader.get('assets/images/tilesets/' + this.tileset + '/icons.png'),
            (iconNum % 5) * 46, ~~(iconNum / 5) * 38, 46, 38, pos[0], pos[1], 46, 38);
    }
};

InfoPanel.prototype.drawCompleteBar = function(unit, pos, vars) {
    // fix dark-green to darkgreen
    var color = vars.Color !== undefined ? vars.Color.replace('-', '') : UI.CompletedBarColorRGB;
    var percent = ~~((100 * unit.vars[vars.Variable1]) / unit.vars[vars.Variable2]);

    if (vars.Border === undefined) {
        this.context.fillStyle = color;
        var computedWidth = ~~((percent * vars.Width) / 100);
        this.context.fillRect(pos[0], pos[1], computedWidth, vars.Height);

        if (UI.CompletedBarShadow) {
            this.context.beginPath();
            this.context.strokeStyle = "gray";
            this.context.moveTo(pos[0] + computedWidth, pos[1]);
            this.context.lineTo(pos[0] + computedWidth, pos[1] + vars.Height);

            this.context.moveTo(pos[0], pos[1] + vars.Height);
            this.context.lineTo(pos[0] + computedWidth, pos[1] + vars.Height);

            this.context.beginPath();
            this.context.strokeStyle = "white";
            this.context.moveTo(pos[0], pos[1]);
            this.context.lineTo(pos[0], pos[1] + vars.Height);

            this.context.moveTo(pos[0], pos[1]);
            this.context.lineTo(pos[0] + computedWidth, pos[1]);
        }
    } else {
        this.context.fillStyle = 'gray';
        this.context.fillRect(pos[0], pos[1], vars.Width + 4, vars.Height);
        this.context.fillStyle = 'black';
        this.context.fillRect(pos[0] + 1, pos[1] + 1, vars.Width + 2, vars.Height - 2);
        this.context.fillStyle = color;
        this.context.fillRect(pos[0] + 2, pos[1] + 2, computedWidth, vars.Height - 4);
    }
};

InfoPanel.prototype.drawButtonBar = function(target) {
    buttons.filter(function(b) { return b.ForUnit.indexOf(target.typeName) > -1 && b.Level == this.level; }, this)
        .forEach(function(b) {
            var iconNum = icons[b.Icon];
            var pos = b.Pos - 1;
            this.drawBox(6 + (pos % 3) * 56, 176 + ~~(pos / 3) * 48, 54, 46,
                ResourcePreloader.get('assets/images/tilesets/' + this.tileset + '/icons.png'),
                (iconNum % 5) * 46, ~~(iconNum / 5) * 38);

            // todo impl
            if (target.orders[0].action == b.Action) {

            }
        }, this);
};

InfoPanel.prototype.draw = function() {
    if (this.isDrawNeeded) {
        this.context.clearRect(0, 0, this.canvas.width, this.canvas.height);
        if (Object.keys(this.selection.targets).length == 1) {
            this.context.drawImage(ResourcePreloader.get('assets/images/ui/' + this.race + '/infopanel.png'), 0, 176,
                176, 176, 0, 0, 176, 176);

            var target = this.selection.targets[Object.keys(this.selection.targets)[0]];
            var iconNum = icons[target.type.Icon];
            this.drawBox(6, 6, 54, 46, ResourcePreloader.get('assets/images/tilesets/' + this.tileset + '/icons.png'),
                (iconNum % 5) * 46, Math.floor(iconNum / 5) * 38);

            function checkCondition(condition, unit) {
                if ((condition.ShowOnlySelected && !unit.selected)
                    || (unit.player.type == "Neutral" && condition.HideNeutral)
                    || (player.isEnemy(unit) && !condition.ShowOpponent)
                    || (player.isAllied(unit) && condition.HideAllied)) {
                    return false;
                }

                var excluded = ["ShowOnlySelected", "HideNeutral", "ShowOpponent", "HideAllied"];

                return Object.keys(condition).filter(function(cond) { return excluded.indexOf(cond) == -1; })
                    .every(function(cond) {
                        if (condition[cond] != "true") {
                            if (condition[cond] == "only" ^ (unit.vars[cond] !== undefined && unit.vars[cond] !== false)) {
                                return false;
                            }
                        }

                        return true;
                    }
                );
            }

            infoPanelContent.forEach(function(panel) {
                if (panel.Condition == undefined || checkCondition(panel.Condition, target)) {
                    this.defaultFont = panel.DefaultFont;

                    panel.Contents.forEach(function(content) {
                        if (content.Condition == undefined || checkCondition(content.Condition, target)) {
                            this["draw" + content.More[0]].call(this, target, content.Pos, content.More[1]);
                        }
                    }, this);
                }
            }, this);

            if (player.isCurrent(target)) {
                this.drawButtonBar(target);
            }
        }

        this.isDrawNeeded = false;
    }
};