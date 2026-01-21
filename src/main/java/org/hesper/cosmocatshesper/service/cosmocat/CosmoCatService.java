package org.hesper.cosmocatshesper.service.cosmocat;

import java.util.List;

public interface CosmoCatService {
    List<String> listCosmoCats();
    String translateMeow(String meow);
    String getPromoBanner();
}
