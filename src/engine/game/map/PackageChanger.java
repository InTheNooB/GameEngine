package engine.game.map;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

class PackageChanger extends ObjectInputStream {

    public PackageChanger(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass resultClassDescriptor = super.readClassDescriptor();

        if (resultClassDescriptor.getName().equals("app.creator.Block")) {
            resultClassDescriptor = ObjectStreamClass.lookup(engine.game.map.Block.class);
        } else if (resultClassDescriptor.getName().equals("app.creator.Map")) {
            resultClassDescriptor = ObjectStreamClass.lookup(engine.game.map.AbstractMap.class);
        }

        return resultClassDescriptor;
    }

}
