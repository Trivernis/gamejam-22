/// <reference types="@mapeditor/tiled-api" />

/*
 * find-layer-by-id.js
 *
 * This extension adds a 'Select Layer by ID' (Ctrl+Shift+L) action to the
 * Layer menu, which can be used to quickly jump to and select a layer when
 * you know its ID.
 */

/* global tiled */

function findLayerById(thing, id) {
	for (let i = thing.layerCount - 1; i >= 0; i--) {
		const layer = thing.layerAt(i);
		if (layer.id == id) {
			return layer;
		}

		if (layer.isGroupLayer) {
			const l = findLayerById(layer, id);
			if (l) {
				return l;
			}
		}
	}

	return null;
}

function getPreviousLayer(id) {
	const map = tiled.activeAsset;
	let resultingLayer = null;
	map.layers.forEach((layer, index) => {
		if(layer.id === id) {
			resultingLayer = map.layers[index -1]
		}
	})
	return resultingLayer;
}

function getPreviousTiledLayer(id) {
	let prevLayer = getPreviousLayer(id);
	while(prevLayer != null && !prevLayer.isTileLayer) {
		prevLayer = getPreviousLayer(prevLayer.id);
	}
	
	return prevLayer
}

function getNextLayer(id) {
	const map = tiled.activeAsset;
	let resultingLayer = null;
	map.layers.forEach((layer, index) => {
		if(layer.id === id) {
			resultingLayer = map.layers[index +1]
		}
	})
	return resultingLayer;
}

function getNextTiledLayer(id) {
	let nextLayer = getNextLayer(id);
	while(nextLayer != null && !nextLayer.isTileLayer) {
		nextLayer = getNextLayer(nextLayer.id);
	}
	
	return nextLayer
}

function moveTilesFromLayerToTarget(region, fromLayer, targetLayer) {
	if(fromLayer == null || targetLayer == null) {
		return []
	}
	tiled.log("Moving tiles from " + region + " in layer " + fromLayer.name + " to " + targetLayer.name);
	const editableFromLayer = fromLayer.edit();
	const editableTargetLayer = targetLayer.edit();
	const tiles = [];
	for(let rect of region.rects) {
		for(let x = rect.x; x < rect.x + rect.width; x++) {
			for (let y = rect.y; y < rect.y + rect.height; y++) {
				const tile = fromLayer.tileAt(x, y);
				
				if(tile != null) {
					editableFromLayer.setTile(x, y, null);
					editableTargetLayer.setTile(x, y, tile);
					tiles.push(tile);
				}
			}
		}
	}
	
	editableFromLayer.apply();
	editableTargetLayer.apply();
	
	return tiles;
}

let moveTilesOneLayerDown = tiled.registerAction("MoveTilesOneLayerDown", function(/* action */) {
	const map = tiled.activeAsset;
	if (!map.isTileMap) {
		tiled.alert("Not a tile map.");
		return;
	}
	
	tiled.log("Menus" + tiled.menus)
	for(let layer of map.layers) {
		tiled.log(`Id: ${layer.id} - name: ${layer.name}`);
	}
	if(map.selectedLayers.length > 1) {
		tiled.alert("Only one selected layer allowed.");
		
		return;
	}
	const selectedLayer = map.selectedLayers[0];
	if(!selectedLayer.isTileLayer) {
	    tiled.alert("You need to select a tiled layer.")
	}
	tiled.log("Selected layer: " + selectedLayer.name);
	const previousLayer = getPreviousTiledLayer(selectedLayer.id)
	tiled.log("Prev layer is: " + previousLayer.name)
	const tiles = moveTilesFromLayerToTarget(map.selectedArea.get(), selectedLayer, previousLayer);
	tiled.alert("Moved " + tiles.length + " tiles");
});

moveTilesOneLayerDown.text = "Move selected Tiles one layer down";
moveTilesOneLayerDown.shortcut = "Ctrl+Alt+L";

let moveTilesOnelayerUp = tiled.registerAction("MoveTilesOneLayerUp", function() {
		const map = tiled.activeAsset;
	if (!map.isTileMap) {
		tiled.alert("Not a tile map!");
		return;
	}
	
	tiled.log("Menus" + tiled.menus)
	for(let layer of map.layers) {
		tiled.log(`Id: ${layer.id} - name: ${layer.name}`);
	}
	if(map.selectedLayers.length > 1) {
		tiled.alert("Only one selected layer allowed");
		
		return;
	}
	const selectedLayer = map.selectedLayers[0];
	tiled.log("Selected layer: " + selectedLayer.name);
	const previousLayer = getNextTiledLayer(selectedLayer.id)
	tiled.log("Next layer is: " + previousLayer.name)
	const tiles = moveTilesFromLayerToTarget(map.selectedArea.get(), selectedLayer, previousLayer);
	tiled.alert("Moved " + tiles.length + " tiles");
});
moveTilesOnelayerUp.text = "move selected Tiles on layer up";
moveTilesOnelayerUp.shortcut = "Ctrl+Alt+O";

tiled.extendMenu("Layer", [
	{ action: "MoveTilesOneLayerDown", before: "SelectPreviousLayer" },
	{ action: "MoveTilesOneLayerUp", before: "MoveTilesOneLayerDown" },
]);