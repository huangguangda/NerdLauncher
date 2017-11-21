package cn.edu.gdmec.android.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jack on 2017/11/20.
 */
//如果要使用RecyclerView, 则必须添加recyclerview-v7,添加依赖库
public class NerdLauncherFragment extends Fragment{
    //向PackageManager查询activity总数
    private static final String TAG = "NerdLauncherFragment";

    //声明
    private RecyclerView mRecyclerView;
    public static NerdLauncherFragment newInstance(){
        return new NerdLauncherFragment ();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate ( R.layout.fragment_nerd_launcher, container, false );
        mRecyclerView = (RecyclerView) v
                .findViewById ( R.id.fragment_nerd_launcher_recycler_view );
        mRecyclerView.setLayoutManager(new LinearLayoutManager (getActivity ()));
        //向PackageManager查询activity总数
        setupAdapter();

        return v;
    }

    //向PackageManager查询activity总数
    //在LogCat日志查看PackageManager返回的activity数目。
    private void setupAdapter() {
        Intent startupIntent = new Intent ( Intent.ACTION_MAIN );
        startupIntent.addCategory ( Intent.CATEGORY_LAUNCHER );

        PackageManager pm = getActivity ().getPackageManager ();
        List<ResolveInfo> activities = pm.queryIntentActivities ( startupIntent, 0 );
        //对activity标签排序
        Collections.sort ( activities, new Comparator<ResolveInfo> () {
            @Override
            public int compare(ResolveInfo a, ResolveInfo b) {
                PackageManager pm = getActivity ().getPackageManager ();
                return String.CASE_INSENSITIVE_ORDER.compare (
                        a.loadLabel ( pm ).toString (),
                        b.loadLabel ( pm ).toString ());
            }
        } );

        Log.i ( TAG, "Found " + activities.size () + " activities." );
        //为RecyclerView设置adapter
        mRecyclerView.setAdapter ( new ActivityAdapter ( activities ) );
    }

    //实现ViewHolder
    //启动目标activity
    private class ActivityHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener{
        private ResolveInfo mResolveInfo;
        private TextView mNameTextView;

        public ActivityHolder(View itemView){
            super(itemView);
            mNameTextView = (TextView) itemView;
            //启动目标activity
            mNameTextView.setOnClickListener ( this );
        }
        public void bindActivity(ResolveInfo resolveInfo){
            mResolveInfo = resolveInfo;
            PackageManager pm = getActivity ().getPackageManager ();
            String appName = mResolveInfo.loadLabel ( pm ).toString ();
            mNameTextView.setText ( appName );
        }
        //启动目标activity
        @Override
        public void onClick(View v){
            ActivityInfo activityInfo = mResolveInfo.activityInfo;

            Intent i = new Intent ( Intent.ACTION_MAIN )
                    .setClassName ( activityInfo.applicationInfo.packageName,
                            activityInfo.name)
                    .addFlags ( Intent.FLAG_ACTIVITY_NEW_TASK );
            //为intent添加新任务标志

            startActivity ( i );
        }

    }

    //实现RecyclerView.Adapter
    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder>{
        private final List<ResolveInfo> mActivities;

        public ActivityAdapter(List<ResolveInfo> activities){
            mActivities = activities;
        }
        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from ( getActivity () );
            View view = layoutInflater
                    .inflate ( android.R.layout.simple_list_item_1, parent, false );
            return new ActivityHolder ( view );
        }
        @Override
        public void onBindViewHolder(ActivityHolder activityHolder, int position){
            ResolveInfo resolveInfo = mActivities.get ( position );
            activityHolder.bindActivity ( resolveInfo );
        }
        @Override
        public int getItemCount(){
            return mActivities.size ();
        }
    }


}
