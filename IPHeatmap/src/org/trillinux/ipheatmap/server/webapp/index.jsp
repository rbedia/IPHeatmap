<html>
<head>
  <title>Gnutella2 Network IP Distribution Map</title>
    <script src="http://openlayers.org/api/OpenLayers.js"></script>
    <script src="hilbert.js"></script>
    <style type="text/css">
        /* avoid pink tiles */
        .olImageLoadError {
            background-color: transparent !important;
        }
    </style>
</head>
    <body>
      IP Address: <input id="ipField" type="text" value="<%= request.getRemoteAddr() %>">
      <input id="search" type="button" value="Locate" onclick="searchClick()">
      <span>Clicked location:</span> <span id="currentLocation"></span>
      <div style="width:100%; height:100%" id="map"></div>
      <script defer="defer" type="text/javascript">
		function num2ip(num) {
			var d = num%256;
			for (var i = 3; i > 0; i--) {
				num = Math.floor(num/256);
				d = num % 256 + '.' + d;
			}
			return d;
		}
		function dot2num(dot) {
			var d = dot.split('.');
			return ((((((+d[0])*256)+(+d[1]))*256)+(+d[2]))*256)+(+d[3]);
		}
		function searchClick() {
			var zoom = map.getZoom();
			var scale = Math.pow(2, zoom - 1) * 256; // one edge length
			var ip = document.getElementById('ipField').value;
			var p = d2xy(Math.pow(2,32), dot2num(ip));
			var x = p[0] / Math.pow(2,16) * scale;
			var y = p[1] / Math.pow(2,16) * scale;
			var lon = (x * 180 / scale) - 180;
			var lat = -(y * 180 / scale - 90);
			var lonlat = new OpenLayers.LonLat(lon, lat);
			map.setCenter(lonlat, 6);
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
                //map.panTo(map.getLonLatFromPixel(evt.xy));
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
					document.getElementById('currentLocation').innerHTML = ip;
					//alert(ip);
				}
            }   

        });

		// Limit the map to the left half since the ip map is square
		var extent = new OpenLayers.Bounds(-180, -90, 0, 90);
            var options = {
                restrictedExtent: extent
            }

        var map = new OpenLayers.Map('map', options);
		var hubLayer = new OpenLayers.Layer.XYZ("Hubs", "tile/hubs?z=\${z}&x=\${x}&y=\${y}", {numZoomLevels: 10, alpha: true, layers: 'basic'});
        map.addLayer(hubLayer);
		var leafLayer = new OpenLayers.Layer.XYZ("Leaves", "tile/leaves?z=\${z}&x=\${x}&y=\${y}", {numZoomLevels: 10, alpha: true, layers: 'basic'});
        map.addLayer(leafLayer);

		var click = new OpenLayers.Control.Click();
		map.addControl(click);
		click.activate();

		var switcherControl = new OpenLayers.Control.LayerSwitcher();
		map.addControl(switcherControl);
		switcherControl.maximizeControl();

        map.zoomToMaxExtent();

      </script>

</body>
</html>

