package hamouanis.interactivestory.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

import hamouanis.interactivestory.R;
import hamouanis.interactivestory.model.*;

public class StoryActivity extends AppCompatActivity {

    private String name;

    private Story story;

    private ImageView storyImageView;
    private TextView storyTextView;
    private Button choice1Button;
    private Button choice2Button;

    private Stack<Integer> pageStack = new Stack<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        storyImageView = (ImageView) findViewById(R.id.storyImageView);
        storyTextView = (TextView) findViewById(R.id.storyTextView);
        choice1Button = (Button) findViewById(R.id.choice1Button);
        choice2Button = (Button) findViewById(R.id.choice2Button);

        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.key_name));
        Toast.makeText(this, "welcome " + name, Toast.LENGTH_SHORT).show();

        story = new Story();

        loadpage(0);


    }

    private void loadpage(int pageNumber) {

        pageStack.push(pageNumber);

        final Page page = story.getPage(pageNumber);

        Drawable image = ContextCompat.getDrawable(this,page.getImageId());
        storyImageView.setImageDrawable(image);

        String pageText = getString(page.getTextId());
        pageText = String.format(pageText,name);
        storyTextView.setText(pageText);

        if (page.isFinalPage()){

            choice1Button.setVisibility(View.INVISIBLE);
            choice2Button.setVisibility(View.VISIBLE);

            choice2Button.setText(R.string.play_again_button_text);
            choice2Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //finish() go back to intent first activity
                    loadpage(0);
                }
            });

        }
        else{

            loadButton(page);

        }


    }

    private void loadButton(final Page page) {

        choice1Button.setVisibility(View.VISIBLE);

        choice1Button.setText(page.getChoice1().getTextId());
        choice1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadpage(page.getChoice1().getNextPage());
            }
        });

        choice2Button.setVisibility(View.VISIBLE);
        choice2Button.setText(page.getChoice2().getTextId());
        choice2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadpage(page.getChoice2().getNextPage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        pageStack.pop();
        if (pageStack.isEmpty()){
            super.onBackPressed();
        }
        else {

            loadpage(pageStack.pop());

        }
    }
}
