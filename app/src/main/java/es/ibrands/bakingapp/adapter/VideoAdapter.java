package es.ibrands.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import es.ibrands.bakingapp.R;
import es.ibrands.bakingapp.model.Step;
import es.ibrands.bakingapp.util.OnClickInterface;

import java.util.List;

/**
 * Created by pablomoreno on 02/02/18.
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder>
{
    private Context mContext;
    private List<Step> stepList;
    private OnClickInterface onClickInterface;

    public VideoAdapter(Context mContext, List<Step> stepList)
    {
        this.mContext = mContext;
        this.stepList = stepList;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
    {
        View view = LayoutInflater.from(mContext).inflate(
            R.layout.video_list,
            viewGroup,
            false
        );

        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder videoViewHolder, final int position)
    {
        String shortDescription = stepList.get(position).getShortDescription();
        videoViewHolder.stepTextView.setText(shortDescription);
        videoViewHolder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onClickInterface.onClick(
                    mContext,
                    stepList.get(position).getId(),
                    stepList.get(position).getDescription(),
                    stepList.get(position).getVideoURL(),
                    stepList.get(position).getThumbnailURL());
            }
        });
    }

    public void setOnClick(OnClickInterface onClickInterface)
    {
        this.onClickInterface = onClickInterface;
    }

    @Override
    public int getItemCount()
    {
        return stepList.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.step_name_text_view)
        TextView stepTextView;

        @BindView(R.id.card_video_list)
        CardView cardView;

        public VideoViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
