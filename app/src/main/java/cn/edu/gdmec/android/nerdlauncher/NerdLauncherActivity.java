package cn.edu.gdmec.android.nerdlauncher;

import android.support.v4.app.Fragment;

//深入学习intent和任务
public class NerdLauncherActivity extends SingleFragmentActivity {
   /* @Override
    protected Fragment createFragment{
        return NerdLauncherFragment.newInstance();
    }*/

    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance ();
    }

   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.fragment_nerd_launcher );
    }*/
}
