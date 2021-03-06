package com.larryngo.shinycollector;

import android.os.Bundle;
import com.larryngo.shinycollector.models.Game;
import com.larryngo.shinycollector.models.Method;
import com.larryngo.shinycollector.models.Platform;
import com.larryngo.shinycollector.models.Pokemon;
import com.larryngo.shinycollector.models.PokemonList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class StartHuntActivity extends AppCompatActivity implements
        GameListFragment.FragmentGameListener,
        PokemonViewFragment.FragmentPokemonViewListener,
        PokemonListFragment.SendPokemonToView,
        PlatformListFragment.FragmentPlatformListListener,
        MethodListFragment.FragmentMethodListListener {

    protected static FragmentManager fm;
    protected StartHuntFragment startHuntFragment;
    protected GameListFragment gameListFragment;
    protected PokemonListFragment pokemonListFragment;
    protected PokemonViewFragment pokemonViewFragment;
    protected PlatformListFragment platformListFragment;
    protected MethodListFragment methodListFragment;

    private static final String ARGUMENT_ACTIVE_HUNT = "ARGUMENT_ACTIVE_HUNT";

    @Override
    public void sendPokemonToView(PokemonList pokemonData) {
        pokemonViewFragment.receiveIndex(pokemonData);
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                .replace(R.id.starthunt_fragment_container, pokemonViewFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onInputGameSent(Game game) {
        startHuntFragment.updateGame(game);
    }

    @Override
    public void onInputPokemonSent(Pokemon pokemon) {
        startHuntFragment.updatePokemon(pokemon);
    }

    @Override
    public void onInputPlatformSent(Platform platform) {
        startHuntFragment.updatePlatform(platform);
    }

    @Override
    public void onInputMethodSent(Method method) {
        startHuntFragment.updateMethod(method);
    }

    public void startFragment(Fragment fragment)
    {

        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down, R.anim.slide_in_down, R.anim.slide_out_up)
                .replace(R.id.starthunt_fragment_container, fragment)
                .commit();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_starthunt);

        fm = getSupportFragmentManager();

        startHuntFragment = new StartHuntFragment();
        gameListFragment = new GameListFragment();
        pokemonListFragment = new PokemonListFragment();
        pokemonViewFragment = new PokemonViewFragment();
        platformListFragment = new PlatformListFragment();
        methodListFragment = new MethodListFragment();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            if(extras.containsKey(ARGUMENT_ACTIVE_HUNT)) {
                startHuntFragment.setArguments(extras);
            }
        }
        startFragment(startHuntFragment);
    }

    @Override
    public void onBackPressed() {
        int backStackCount = fm.getBackStackEntryCount(); //gets count of the back stack
        if (backStackCount != 0) //if theres a current back stack
        {
            fm.popBackStack(); //pops the stack, reformats the layout
        }
        else
        {
            super.onBackPressed();
        }
    }
}
