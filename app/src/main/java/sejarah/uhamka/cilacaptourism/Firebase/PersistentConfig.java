package sejarah.uhamka.cilacaptourism.Firebase;

import com.google.firebase.database.FirebaseDatabase;

public class PersistentConfig extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
