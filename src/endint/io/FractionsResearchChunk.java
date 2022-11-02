package endint.io;

import arc.util.io.Reads;
import arc.util.io.Writes;
import endint.ResearchController;
import mindustry.io.SaveFileReader;
import mindustry.io.SaveVersion;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FractionsResearchChunk implements SaveFileReader.CustomChunk {
    @Override
    public void write(DataOutput stream) throws IOException {
        Writes write = new Writes(stream);
        ResearchController.save(write);
    }

    @Override
    public void read(DataInput stream) throws IOException {
        Reads read = new Reads(stream);
        ResearchController.load(read);
    }

    public FractionsResearchChunk(){
        SaveVersion.addCustomChunk("FractionsResearchChunk", this);
    }
}
