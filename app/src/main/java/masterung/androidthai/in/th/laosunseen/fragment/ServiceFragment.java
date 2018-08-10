package masterung.androidthai.in.th.laosunseen.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import masterung.androidthai.in.th.laosunseen.R;
import masterung.androidthai.in.th.laosunseen.utility.MyAlert;
import masterung.androidthai.in.th.laosunseen.utility.ServiceAdapter;
import masterung.androidthai.in.th.laosunseen.utility.UserModel;

public class ServiceFragment extends Fragment {

    private String nameString, currentPostString, uidString;
    private String tag = "10AugV1";

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findMyMe();

//        Post Controller
        postController();

//        Create RecyclerView123
        createRecyclerView();

    }   // Main Method

    private void createRecyclerView() {

        final RecyclerView recyclerView = getView().findViewById(R.id.recyclerViewUser);
        final int[] countInts = new int[]{0};

        final ArrayList<String> photoStringArrayList = new ArrayList<>();
        final ArrayList<String> nameStringArrayList = new ArrayList<>();
        final ArrayList<String> postStringArrayList = new ArrayList<>();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("User");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = (int) dataSnapshot.getChildrenCount();
                ArrayList<UserModel> modelArrayList = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    UserModel userModel = dataSnapshot1.getValue(UserModel.class);
                    modelArrayList.add(userModel);

                    UserModel userModel1 = modelArrayList.get(countInts[0]);
                    countInts[0] += 1;

                    photoStringArrayList.add(userModel1.getPathUrlString());
                    nameStringArrayList.add(userModel1.getNameString());
                    postStringArrayList.add(userModel1.getMyPostString());


                }   //for

                ServiceAdapter serviceAdapter = new ServiceAdapter(getActivity(),
                        photoStringArrayList, nameStringArrayList, postStringArrayList);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                        LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(serviceAdapter);

            }   // dataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }   // createRecycler

    private void findMyMe() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        uidString = firebaseAuth.getCurrentUser().getUid();
        Log.d(tag, "uid ==> " + uidString);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("User").child(uidString);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Map map = (Map) dataSnapshot.getValue();
                nameString = String.valueOf(map.get("nameString"));
                currentPostString = String.valueOf(map.get("myPostString"));
                Log.d(tag, "Name ==> " + nameString);
                Log.d(tag, "currentPost ==> " + currentPostString);
                Toast.makeText(getActivity(), "Name ==> " + nameString, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void postController() {
        Button button = getView().findViewById(R.id.btnPost);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = getView().findViewById(R.id.edtPost);
                String postString = editText.getText().toString().trim();
                if (postString.isEmpty()) {
                    MyAlert myAlert = new MyAlert(getActivity());
                    myAlert.normalDialog("Post False",
                            "Please Type on Post");
                } else {
                    editCurrentPost(postString);
                    editText.setText("");
                }
            }
        });
    }

    private void editCurrentPost(String postString) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference()
                .child("User").child(uidString);

        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("myPostString", changeMyData(postString));
        databaseReference.updateChildren(stringObjectMap);


    }

    private String changeMyData(String postString) {

        String resultString = null;

        resultString = currentPostString.substring(1, currentPostString.length() - 1);
        String[] strings = resultString.split(",");
        ArrayList<String> stringArrayList = new ArrayList<>();
        for (int i = 0; i < strings.length; i += 1) {
            stringArrayList.add(strings[i].trim());
        }
        stringArrayList.add(postString);
        return stringArrayList.toString();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service, container, false);
        return view;
    }
}
