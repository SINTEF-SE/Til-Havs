
// Instantiating planned seismic activity layer
const iceConcentrationSource = Sintium.dataSource({
    url: "https://www.barentswatch.no/api/v1/geodata/download/icechart/?format=JSON",
});

const closeDriftIceStyle = new ol.style.Style({
    fill: new ol.style.Fill({ color: "rgba(251, 156, 69, 0.5)" })
});
const veryCloseDriftIceStyle = new ol.style.Style({
    fill: new ol.style.Fill({ color: "rgba(255, 64, 64, 0.5)" })
});

const fastIceStyle = new ol.style.Style({
    fill: new ol.style.Fill({ color: "rgba(195, 197, 199, 0.5)" })
});

const openDriftIceStyle = new ol.style.Style({
    fill:  new ol.style.Fill({ color: "rgba(255, 255, 64, 0.5)" })
});

const veryOpenDriftIceStyle = new ol.style.Style({
    fill:  new ol.style.Fill({ color: "rgba(165, 253, 184, 0.5)" })
});

const openWaterStyle = new ol.style.Style({
    fill: new ol.style.Fill({ color: "rgba(176, 214, 255, 0.5)" })
});

function iceChartStyleFunction(feature) {
    const key = feature.getFeatureKey();
    const record = iceConcentrationSource
        .getDataContainer()
        .getRecord(key);

    switch (record.get("icetype")) {
        case "Close Drift Ice":
            return closeDriftIceStyle;
        case "Very Close Drift Ice":
            return veryCloseDriftIceStyle;
        case "Fast Ice":
            return fastIceStyle;
        case "Open Drift Ice":
            return openDriftIceStyle;
        case "Very Open Drift Ice":
            return veryOpenDriftIceStyle;
        case "Open Water":
            return openWaterStyle;
        default:
            return closeDriftIceStyle;
    }
}

const iceConcentrationLayer = Sintium.vectorLayer2({
    layerId: "Iskonsentrasjon",
    dataSource: iceConcentrationSource,
    visible: false,
    styleFunction: iceChartStyleFunction
});

// Instantiating planned seismic activity layer
const iceEdgeSource = Sintium.dataSource({
    url: "https://www.barentswatch.no/api/v1/geodata/download/iceedge/?format=JSON"
});

const iceEdgeLayer = Sintium.vectorLayer2({
    layerId: "Iskant",
    dataSource: iceEdgeSource,
    visible: false,
    style: {
        single: {
            fillColor: "#7cb5ec",
            strokeSize: 4
        }
    }
});

// Instantiating ice group
const iceGroup = Sintium.layerGroup("Is", iceConcentrationLayer, iceEdgeLayer);