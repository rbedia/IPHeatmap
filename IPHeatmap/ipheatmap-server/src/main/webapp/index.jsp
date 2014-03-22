<%@page import="org.trillinux.ipheatmap.server.LayerRegistrar"%>
<%@page import="org.trillinux.ipheatmap.server.Layer"%>
<html>
    <head>
        <title><%= application.getInitParameter("title") %></title>
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
            addLayer("<%=layer.getName()%>", "<%=layer.getPath()%>", <%=layer.isBaseLayer() %>);
            <%
            }
            %>
            map.zoomToMaxExtent();
        }
        </script>
        <style type="text/css">
            #map {
                width: 100%;
                height: 100%;
            }
            /* avoid pink tiles */
            .olImageLoadError {
                background-color: transparent !important;
            }
            #controls {
                padding-bottom: 4px;
            }
            #about {
                float: right;
            }
        </style>
    </head>
    <body onload="start();">
        <div id="controls">
            IP Address: <input id="ipField" type="text" value="<%= request.getRemoteAddr() %>">
            <input id="search" type="button" value="Locate" onclick="searchClick();">
            <span>Clicked location:</span> <input id="currentLocation" type="text" readonly="readonly">
            <div id="about"><a href="https://github.com/rbedia/IPHeatmap">IPHeatmap</a></div>
        </div>
        <div id="map"></div>
    </body>
</html>
