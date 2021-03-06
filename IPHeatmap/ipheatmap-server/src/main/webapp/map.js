
function searchClick() {
    var searchText = document.getElementById('ipField').value;
    var cidrRegexp = /(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})\/(\d{1,2})/;
    var match = cidrRegexp.exec(searchText);
    if (match) {
        var ip = match[1];
        var lonlat = ip2lonlat(ip);
        var cidr = +match[2]; // Convert string to number
        var zoom = (cidr / 2) + 2;
        map.setCenter(lonlat, zoom);
    } else {
        var ip = searchText;
        var lonlat = ip2lonlat(ip);
        map.setCenter(lonlat, 6);
    }
}

function ip2lonlat(ip) {
    var zoom = map.getZoom();
    var scale = Math.pow(2, zoom - 1) * 256; // one edge length
    var p = d2xy(Math.pow(2,32), dot2num(ip));
    var x = p[0] / Math.pow(2,16) * scale;
    var y = p[1] / Math.pow(2,16) * scale;
    var lon = (x * 180 / scale) - 180;
    var lat = -(y * 180 / scale - 90);
    var lonlat = new OpenLayers.LonLat(lon, lat);
    return lonlat;
}

function addLayer(name, path, isBaseLayer) {
    var layer = new OpenLayers.Layer.XYZ(name, path, {numZoomLevels: 10, alpha: true, layers: 'basic'});
    layer.setIsBaseLayer(isBaseLayer);
    map.addLayer(layer);
}

OpenLayers.Control.Click = OpenLayers.Class(OpenLayers.Control, {                
    defaultHandlerOptions: {
        'single': true,
        'delay': 200
    },

    initialize: function(options) {
        this.handlerOptions = OpenLayers.Util.extend(
            {}, this.defaultHandlerOptions
        );
        OpenLayers.Control.prototype.initialize.apply(
            this, arguments
        ); 
        this.handler = new OpenLayers.Handler.Click(
            this, {
                'click': this.onClick 
            }, this.handlerOptions
        );
    }, 

    onClick: function(evt) {
        var latlon = map.getLonLatFromPixel(evt.xy);
        var zoom = map.getZoom();
        var scale = Math.pow(2, zoom - 1) * 256; // one edge length
        // note: lon is being scaled by 180 because the IP map is square
        // and only half of the geographic space is in use
        var x = scale * ((latlon.lon + 180) / 180);
        var y = scale * (1 - (latlon.lat + 90) / 180);
        if (x >= 0 && x < scale && y >= 0 && y < scale) {
            var s = xy2d(scale, x, y);
            var ip = num2ip(s / (scale * scale) * Math.pow(2, 32));
            document.getElementById('currentLocation').value = ip;
        }
    }

});

function createMap() {
    // Limit the map to the left half since the ip map is square
    var extent = new OpenLayers.Bounds(-180, -90, 0, 90);
    var options = {
        restrictedExtent: extent
    };

    var map = new OpenLayers.Map('map', options);

    var click = new OpenLayers.Control.Click();
    map.addControl(click);
    click.activate();

    var switcherControl = new OpenLayers.Control.LayerSwitcher();
    map.addControl(switcherControl);
    switcherControl.maximizeControl();

    return map;
}