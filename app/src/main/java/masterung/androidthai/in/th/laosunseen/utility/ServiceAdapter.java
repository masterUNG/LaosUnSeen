package masterung.androidthai.in.th.laosunseen.utility;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import masterung.androidthai.in.th.laosunseen.R;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>{

    private Context context;
    private ArrayList<String>  photoStringArrayList, nameStringArrayList, postStringArrayList;
    private LayoutInflater layoutInflater;

    public ServiceAdapter(Context context, ArrayList<String> photoStringArrayList, ArrayList<String> nameStringArrayList, ArrayList<String> postStringArrayList) {
        this.layoutInflater = LayoutInflater.from(context);
        this.photoStringArrayList = photoStringArrayList;
        this.nameStringArrayList = nameStringArrayList;
        this.postStringArrayList = postStringArrayList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.recycler_view_service, viewGroup, false);
        ServiceViewHolder serviceViewHolder = new ServiceViewHolder(view);
        return serviceViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder serviceViewHolder, int i) {

        String urlPathString = photoStringArrayList.get(i);
        String nameString = nameStringArrayList.get(i);
        String postString = postStringArrayList.get(i);

        serviceViewHolder.nameTextView.setText(nameString);
        serviceViewHolder.postTextView.setText(postString);

        Picasso
                .get()
                .load(urlPathString)
                .resize(150, 150)
                .into(serviceViewHolder.circleImageView);


    }

    @Override
    public int getItemCount() {
        return nameStringArrayList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView circleImageView;
        private TextView nameTextView, postTextView;


        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circlePhoto);
            nameTextView = itemView.findViewById(R.id.txtName);
            postTextView = itemView.findViewById(R.id.txtPost);

        }
    }   // Service Class

}   // Main Class
