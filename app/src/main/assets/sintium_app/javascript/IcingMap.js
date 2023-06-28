function closeInfoDrawer(e) {
    e.getMap().getSelectionHandler().clearSelection();
    infoDrawer.close();
    //vesselInfoDrawer.close();
}

// Instantiating layer switcher

const zoomControl = Sintium.zoomControl();

const layerSwitcher = Sintium.sidebarLayerSwitcher({
    position: "left",
    dataLayerLabel: 'Datalag',
    baseLayerLabel: 'Kartlag'
});

const seaMapLayer = Sintium.wmsLayer({
    layerId: "Sjøkart",
    url: "https://opencache.statkart.no/gatekeeper/gk/gk.open?",
    params: {
        LAYERS: 'sjokartraster',
        VERSION: '1.1.1'
    },
    visible: true
});

const baseLayer = Sintium.baseLayer({
    layerId: "Verdenskart",
    layer: Sintium.getDefaultLightTileLayer(),
    darkLayer: Sintium.getDefaultDarkTileLayer()
});

// Instantiating map
const map = Sintium.map({
    domId: "map",
    layers: [baseLayer, seaBottomInstallationsLayer, maritimeBordersLayer, fishRegulationsGroup, seismicGroup, iceGroup, tradeAreaGroup, seaMapLayer],
    use: [infoDrawer, layerSwitcher],
    controls: [zoomControl],
    zoomOnClusterClick: true,
    onClickEmptySpace: closeInfoDrawer,
    onClickCluster: closeInfoDrawer,
    hitTolerance: 4,
    rotation: false
});

var icingSource = new ol.source.TileWMS({
    url: 'https://thredds.met.no/thredds/wms/vesselicing/2021/12/05/vessel_icing_fine_20211205T12Z.nc',
    params: {
        'LAYERS': 'vessel_icing',
        'TILED': true,
        "TIME": "2021-12-06T00:00:00.000Z",
        "STYLES": "boxfill/occam",
        "COLORSCALERANGE": "0,1.34",
        "NUMCOLORBANDS": 3,
        "TRANSPARENT": true,
        "FORMAT": "image/png"
    }
});

var icingLayer = new ol.layer.Tile({
    title: 'Ising',
    source: icingSource,
});

map.getMap().addLayer(icingLayer)

function addToolsAndVesselsLayerToMap() {
    map.layer(toolsLayer);
    map.layer(vesselsLayer);
    map.use(vesselInfoDrawer);
}
