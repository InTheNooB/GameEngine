package engine.game.animation;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class Deserialiser extends ObjectInputStream {

    public Deserialiser(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected java.io.ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass desc = super.readClassDescriptor();
        if (desc.getName().equals("app.wrk.animation.Animation")) {
            return ObjectStreamClass.lookup(engine.game.animation.Animation.class);
        } else if (desc.getName().equals("app.wrk.animation.Timer")) {
            return ObjectStreamClass.lookup(engine.game.animation.Timer.class);
        } else if (desc.getName().equals("app.wrk.animation.Sprite")) {
            return ObjectStreamClass.lookup(engine.game.animation.Sprite.class);
        } else if (desc.getName().equals("app.wrk.animation.AnimationSet")) {
            return ObjectStreamClass.lookup(engine.game.animation.AnimationSet.class);
        }
        return desc;
    }
}
