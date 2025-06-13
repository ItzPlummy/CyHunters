package com.plummy.cyhunters.Iterfaces;

import com.plummy.cyhunters.Enums.KitType;

public interface IKitCreatorFactory {
    IKitCreator createKitCreator(KitType type);
}
