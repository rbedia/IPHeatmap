<%@page import="org.trillinux.ipheatmap.server.LayerRegistrar"%>
<%@page import="org.trillinux.ipheatmap.server.Layer"%>
<html>
	<head>
        <title>Gnutella2 Network IP Distribution Map</title>
	    <script type="text/javascript" src="http://openlayers.org/api/OpenLayers.js"></script>
	    <script type="text/javascript" src="hilbert.js"></script>
	    <script type="text/javascript" src="ip.js"></script>
	    <script type="text/javascript" src="map.js"></script>
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
		var map = createMap();
		<%
		for (Layer layer: LayerRegistrar.getInstance().getLayers()) {
		%>
		map.addLayer(new OpenLayers.Layer.XYZ("<%=layer.getName()%>", "<%=layer.getPath()%>", {numZoomLevels: 10, alpha: true, layers: 'basic'}));
		<%    
		}
		%>
		map.zoomToMaxExtent();
		</script>
    </body>
</html>

