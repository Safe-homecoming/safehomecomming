package com.example.safehomecoming.safeGurad;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.safehomecoming.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter_Accept_Guard extends RecyclerView.Adapter<RecyclerAdapter_Accept_Guard.ItemViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<Accept_Data> listData = new ArrayList<>();



    //============================================================================================
    // EVENT 관련
    public interface Guard_viewClickListener{
        void onAcceptClicked(View v, int position, int idx, int leftkm);
    }

    private Guard_viewClickListener mListener = null;

    // 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnMemo_ViewClickListener(Guard_viewClickListener listener){
        this.mListener = listener;
    }
    //============================================================================================

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.guard_accept_list, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(Accept_Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;// 요청자 이름
        private TextView leftkm;  //안심이와 요청지 까지의 거리
        private TextView workkm;  //요청지와 목적지 사이의 거리
        private ImageView gender;
        private Button acceptBtn;

        ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            leftkm = itemView.findViewById(R.id.textView12);   // 남은 km 수
            workkm = itemView.findViewById(R.id.textView22);  // 남겨둔 textview
            gender = itemView.findViewById(R.id.genderimage); // 성별 icon
            acceptBtn =itemView.findViewById(R.id.acceptbtn);  // 수락 버튼


        }

        void onBind(final Accept_Data data) {
            name.setText(data.getName());
            leftkm.setText(data.getLeftKm()+"m");
            workkm.setText(data.getWorkKm()+"m");
            gender.setImageResource(data.getResId());

            //수락 버튼을 눌렀을 경우
            acceptBtn.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onAcceptClicked(v,pos, data.getIdx(), data.getLeftKm());
                        }
                    }
                }
            });
        }
    }
}
