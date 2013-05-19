window.requestAnimFrame = (function(){
    return  window.requestAnimationFrame       ||
        window.webkitRequestAnimationFrame ||
        window.mozRequestAnimationFrame    ||
        window.oRequestAnimationFrame      ||
        window.msRequestAnimationFrame     ||
        function(/* function */ callback, /* DOMElement */ element){
            window.setTimeout(callback, 1000 / 60);
        };
})();

function to2DArray(_1Darray, columns, rows) {
    var _2Darray = [];
    for (var i = 0; i < rows; i++) {
        _2Darray[i] = _1Darray.slice(i * columns, (i + 1) * columns);
    }

    return _2Darray;
}

function create2dArray(height, width, defaultValue) {
    var array = [];
    for (var i = 0; i < height; i++) {
        array[i] = [];
        for (var j = 0; j < width; j++) {
            array[i][j] = defaultValue;
        }
    }
    return array;
}

function isObjectsEqual(object1, object2) {
    if (object1.constructor != Object || object2.constructor != Object || !isArraysEqual(Object.keys(object1), Object.keys(object2))) {
        return false;
    } else {
        for (var prop in object1) {
            if (object1[prop] != object2[prop]) {
                return false;
            }
        }

        return true;
    }
}

function isArraysEqual(array1, array2) {
    if (array1.constructor != Array || array2.constructor != Array || array1.length != array2.length) {
        return false;
    } else {
        for (var i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }

        return true;
    }
}

function groupBy(array, callback, thisObject) {
    var map = {};
    array.forEach(function(o) {
        var key = callback.call(thisObject, o);

        if (!map[key]) {
            map[key] = [];
        }

        map[key].push(o);
    });

    return map;
}

function selectRandom(array) {
    return array[~~(Math.random() * array.length)];
}

function capitalize(s) {
    return s.substring(0, 1).toUpperCase() + s.substring(1);
}

function getColor(array, index) {
    return array[index] + (array[index + 1] << 8) + (array[index + 2] << 16) + (array[index + 3] << 24);
}

function nameOf(_enum, value) {
    for (var key in _enum) {
        if (_enum.hasOwnProperty(key) && _enum[key] == value) {
            return key;
        }
    }

    return null;
}

function rgbToCss(rgbArray) {
    return '#' + rgbArray.map(function(e) {
        var s = e.toString(16);
        return s.length < 2 ? '0' + s : s;
    }).join("");
}

function normalize(startX, startY, endX, endY) {
    return { startX: Math.min(startX, endX), startY: Math.min(startY, endY),
        width: Math.max(startX, endX) - Math.min(startX, endX),
       height: Math.max(startY, endY) - Math.min(startY, endY) }
}

function checkIsPointInsideBox(x, y, box) {
    return x >= box.startX && x <= box.startX + box.width && y >= box.startY && y <= box.startY + box.height;
}

function isIntersectOrInside(box1, box2) {
    return checkIsPointInsideBox(box2.startX, box2.startY, box1) ||
           checkIsPointInsideBox(box2.startX + box2.width, box2.startY, box1) ||
           checkIsPointInsideBox(box2.startX, box2.startY + box2.height, box1) ||
           checkIsPointInsideBox(box2.startX + box2.width, box2.startY + box2.height, box1) ||
           (box1.startX >= box2.startX && box1.startX + box1.width <= box2.startX + box2.width &&
            box2.startY >= box1.startY && box2.startY + box2.height <= box1.startY + box1.height) ||
           (box2.startX >= box1.startX && box2.startX + box2.width <= box1.startX + box1.width &&
            box1.startY >= box2.startY && box1.startY + box1.height <= box2.startY + box2.height) ||
            checkIsPointInsideBox(box1.startX, box1.startY, box2) ||
            checkIsPointInsideBox(box1.startX + box1.width, box1.startY, box2) ||
            checkIsPointInsideBox(box1.startX, box1.startY + box1.height, box2) ||
            checkIsPointInsideBox(box1.startX + box1.width, box1.startY + box1.height, box2);
}

function alignTo(number, to) {
    return Math.floor(number / to) * to;
}

var keymap = [];

keymap[37] = "left";
keymap[38] = "up";
keymap[39] = "right";
keymap[40] = "down";

var ResourcePreloader = new Preloader();
function Preloader() {
    this.images = [];
    this.cache = {};
    this.preloaded = 0;
}

Preloader.prototype.add = function(path) {
    this.images.push(path);
};

Preloader.prototype.load = function(path) {
    var image = new Image();
    image.onload = this.oncomplete.bind(this, path);
    image.onerror = this.oncomplete.bind(this, path);
    image.onabort = this.oncomplete.bind(this, path);
    image.src = path;
};

Preloader.prototype.oncomplete = function(path, e) {
    if (e.type == 'error') {
        console.log(path);
    }

    this.preloaded++;
    this.cache[path] = e.target;

    if (this.preloaded == this.images.length && this.callback) {
        this.callback();
    }
};

Preloader.prototype.get = function(path) {
    return this.cache[path];
};

Preloader.prototype.loadAll = function(callback) {
    this.callback = callback;
    this.images.forEach(function(image) { this.load(image); }, this);
};