package com.larryngo.shinyhunter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SearchView;

import com.larryngo.shinyhunter.models.Game;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.larryngo.shinyhunter.StartHuntActivity.fm;

public class GameListFragment extends Fragment {
    private ArrayList<Game> list_games = new ArrayList<>();
    private List<String> list_games_names;
    private List<String> list_games_tokens;

    private View view;
    private SearchView sv;
    private GridView gv;
    private GameListAdapter adapter;

    private FragmentGameListener listener;

    public interface FragmentGameListener {
        void onInputGameSent(Game entry) throws IOException;
    }


    /*
        OVERVIEW

        Creates a list of all the current games and puts it into a gridview with an image and the name
        of the game.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.game_list_layout, container, false);
            sv = view.findViewById(R.id.game_list_search);
            gv = view.findViewById(R.id.game_list_grid);
            list_games_names = Arrays.asList(getResources().getStringArray(R.array.list_games_names)); //names of the games
            list_games_tokens = Arrays.asList(getResources().getStringArray(R.array.list_games_tokens)); //file names of the games

            //gridview clicks
            gv.setOnItemClickListener((parent, view, position, id) -> {
                try {
                    Game entry = list_games.get(position); //gets the current game selected
                    listener.onInputGameSent(entry); //sends the game to the main hunt menu
                    fm.popBackStack(); //go back
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            adapter = new GameListAdapter(this.getContext(), list_games);
            gv.setAdapter(adapter);
            setupGrid();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    void setupGrid(){
        if(getActivity() == null) return;
        getActivity().runOnUiThread(() -> {
            final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.startLoadingDialog();
            loadingDialog.setMessage("Loading games...");
            for (int i = 0; i < list_games_tokens.size(); i++)
            {
                try{
                    if(getContext() != null)
                    {
                        String token = list_games_tokens.get(i);

                        InputStream is = getContext().getAssets().open("icons/games/" + token + ".png"); //searches the game based on the token (file name)
                        byte[] image = new byte[is.available()]; //creates image
                        is.read(image); //necessary in order for the image to appear
                        is.close();

                        Game game = new Game(list_games_names.get(i), image, determineGeneration(token)); //creates game
                        list_games.add(game); //sets up the list of games
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
            loadingDialog.dismissDialog();
        });
    }

    //Determines the generation of the game. Can be used to determine what pokemon
    //can be acquired. (Red/Blue/Yellow can go up to 151, G/S/C up to 251, etc.)
    public int determineGeneration(String token) {
        switch(token) {
            case "Red":
            case "Blue":
            case "Green":
            case "Yellow":
            case "Pikachu":
            case "Eevee":
                return 1;
            case "Gold":
            case "Silver":
            case "Crystal":
                return 2;
            case "Ruby":
            case "Sapphire":
            case "Emerald":
            case "FireRed":
            case "LeafGreen":
            case "Colosseum":
            case "XD-Gale-of-Darkness":
                return 3;
            case "Diamond":
            case "Pearl":
            case "Platinum":
            case "HeartGold":
            case "SoulSilver":
            case "Rumble":
                return 4;
            case "Black":
            case "White":
            case "Black-2":
            case "White-2":
                return 5;
            case "X":
            case "Y":
            case "Omega-Ruby":
            case "Alpha-Sapphire":
                return 6;
            case "Sun":
            case "Moon":
            case "Ultra-Sun":
            case "Ultra-Moon":
                return 7;
            case "Sword":
            case "Shield":
                return 8;
        }
        return -1;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentGameListener) {
            listener = (FragmentGameListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentGameListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
