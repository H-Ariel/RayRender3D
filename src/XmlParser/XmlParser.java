package XmlParser;

import scene.Scene;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;


/**
 * Class to parse XML files
 * Note: this class is a bonus :)
 */
public class XmlParser {
    /**
     * Load a scene from an XML file
     *
     * @param filePath the path to the XML file
     * @return the scene object
     */
    public static Scene loadSceneFromXml(String filePath) {
        try {
            JAXBContext context = JAXBContext.newInstance(Scene.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Scene scene = (Scene) unmarshaller.unmarshal(new File(filePath));
            scene.geometries.makeBVH();
            return scene;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Save a scene to an XML file
     *
     * @param scene    the scene object
     * @param filePath the path to the XML file
     */
    static public void saveSceneToXml(Scene scene, String filePath) {
        try {
            Scene sceneToXml = new Scene(scene.name);
            sceneToXml.background = scene.background;
            sceneToXml.ambientLight = scene.ambientLight;
            sceneToXml.geometries = scene.geometries.flattenGeometries();
            sceneToXml.lights = scene.lights;

            JAXBContext context = JAXBContext.newInstance(Scene.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(sceneToXml, new File(filePath));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
