<%@page import="org.trillinux.ipheatmap.server.LayerRegistrar"%>
<%@page import="org.trillinux.ipheatmap.server.Layer"%>
<html>
    <head>
    <title>Gnutella2 Network IP Distribution Map</title>
        <script type="text/javascript" src="http://openlayers.org/api/OpenLayers.js"></script>
        <script type="text/javascript" src="hilbert.js"></script>
        <script type="text/javascript" src="ip.js"></script>
        <script type="text/javascript" src="map.js"></script>
        <script type="text/javascript">
        var map;

        function start() {
            map = createMap();
            <%
            for (Layer layer : LayerRegistrar.getInstance().getLayers()) {
            %>
            addLayer("<%=layer.getName()%>", "<%=layer.getPath()%>", true);
            <%
            }
            %>
            addLayer('Labels', 'tile/labels/\${z}/\${x}/\${y}.png', false);
            map.zoomToMaxExtent();
        }
        </script>
        <style type="text/css">
            /* avoid pink tiles */
            .olImageLoadError {
                background-color: transparent !important;
            }
        </style>
    </head>
    <body onload="start();">
        <div id="controls">
			IP Address: <input id="ipField" type="text" value="<%= request.getRemoteAddr() %>">
			<input id="search" type="button" value="Locate" onclick="searchClick();">
                        <span>Clicked location:</span> <input id="currentLocation" type="text" readonly="readonly">
		</div>
		<div style="width:100%; height:100%" id="map"></div>
    </body>
</html>
