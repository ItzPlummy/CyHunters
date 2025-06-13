package com.plummy.cyhunters.KitCreator;

import com.plummy.cyhunters.Enums.KitType;
import com.plummy.cyhunters.Iterfaces.IKitCreator;
import com.plummy.cyhunters.Iterfaces.IKitCreatorFactory;

public class KitCreatorFactory implements IKitCreatorFactory {
    public KitCreatorFactory() {}

    @Override
    public IKitCreator createKitCreator(KitType type) {
        return switch (type) {
            case EMPTY -> new EmptyKitCreator();
            case BASIC -> new BasicKitCreator();
            case BOW -> new BowKitCreator();
            case SHEARS -> new ShearsKitCreator();
            case OP -> new OpKitCreator();
            case MACE -> new MaceKitCreator();
        };
    }
}
