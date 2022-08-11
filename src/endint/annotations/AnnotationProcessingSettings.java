package endint.annotations;

import endint.*;
import mma.annotations.ModAnnotations.*;


/**
 * If you have no sprites, music and sounds in your mod, remove the annotation after this line
 */
@ModAssetsAnnotation
@RootDirectoryPath(rootDirectoryPath = "")
@AnnotationSettings(
rootPackage = "endint",
assetsPath = "resources/assets",
modInfoPath = "resources/mod.hjson",
assetsRawPath = "resources/assets-raw",
classPrefix = "EI"
)
class AnnotationProcessingSettings{
}
