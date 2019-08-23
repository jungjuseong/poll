export class StoreEncoder {

    static get Version() {
        readJson('../../package.json', console.error, false, (error, package_json) => {
            if (error) {
              console.error("There was an error reading package.json")
              return;
            }
        
            if (package_json.hasOwnProperty("version")) {
                return package_json.version;
            } 
            else 
                return '0.1.0';
        });
    }

    static encodeStore(json) {

        const {pages} = json;

        for (const pageNumber in pages) {
            if (pages.hasOwnProperty(pageNumber)) {
                const { render } = pages[pageNumber];

                const newRender = StoreEncoder.encodeRender(render);
                const layers = StoreEncoder.encodeEachPage(pages[pageNumber]);

                pages[pageNumber] = {...pages[pageNumber], layers, render: newRender};
            }
        }

        return { version: StoreEncoder.Version, ...json, pages };
    }

    static encodeEachPage(page) {

        const { layers } = page;

        const newLayers = {};
        for (const layerNumber in layers) {
            if (layers.hasOwnProperty(layerNumber)) {
                const { render } = layers[layerNumber];
                newLayers[layerNumber] = { ...layers[layerNumber], render: StoreEncoder.encodeRender(render) };
            }
        }
        return newLayers;
    }

    static encodeRender(render) {

        const newRender = []
        for (const key in render) {
            const newD = StoreEncoder.encodeDrawing(render[key]);
            newRender.push(newD);
        }

        return newRender;
    }

    static encodeDrawing(drawing) {
        const { points } = drawing;
        
        const np = [ 
            f3(points.p1.x), f3(points.p1.y), 
            f3(points.p2.x), f3(points.p2.y), 
            f3(points.p3.x), f3(points.p3.y), 
            f3(points.p4.x), f3(points.p4.y) 
        ];

        const { imagePosition, imageSize } =  drawing;
        const { polylinePoints } = drawing;

        if (polylinePoints && polylinePoints.length > 0) {
            const ppp = [];
            for (const v in polylinePoints) {
                const pp = polylinePoints[v];
                ppp.push(f3(pp.x));
                ppp.push(f3(pp.y))
            }
            drawing = { ...drawing, "polylinePoints": ppp };
        }

        const image = [ imagePosition.x, imagePosition.y, imageSize.imageWidth, imageSize.imageHeight]
        
        drawing = { ...drawing, points: np, image };
        
        delete drawing.imagePosition;
        delete drawing.imageSize;

        return drawing;
    }
}