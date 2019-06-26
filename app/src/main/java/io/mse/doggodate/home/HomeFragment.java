package io.mse.doggodate.home;

import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.mse.doggodate.OpenWeatherAPI;
import io.mse.doggodate.entity.Doggo;
import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.main.MainActivity;
import io.mse.doggodate.R;
import io.mse.doggodate.adapters.EventAdapter;
import io.mse.doggodate.profile.ProfileFirestoreCallback;
import io.mse.doggodate.profile.ProfileViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private MainActivity mainActivity;
    private HomeViewModel homeViewModel;
    private TextView weather;
    private  TextView gowalk;
    private TextView weatherIcon;
    private Typeface weatherFont;
    private ProfileViewModel profileViewModel;
    String OPEN_WEATHER_MAP_API ="b9c1970e210e7c88afeb6b9afc432480";
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainActivity = (MainActivity)getActivity();
        mainActivity.getSupportActionBar().setTitle("DoggoDate");

        homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);

        RecyclerView recList = (RecyclerView) view.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(RecyclerView.VERTICAL);
        recList.setLayoutManager(llm);

        EventAdapter ca = new EventAdapter(createEventList(15), new EventAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(DoggoEvent item) {
                selectTimeOfJoining(item);
            }});

        recList.setAdapter(ca);

         weather = (TextView) view.findViewById(R.id.weather);
         gowalk = (TextView) view.findViewById(R.id.goforwalk);
        TextView lastwalk = (TextView) view.findViewById(R.id.lastwalk);
        lastwalk.setText("The last walk was 29 hours ago!");
        weatherIcon = (TextView) view.findViewById(R.id.weather_icons);
        weatherFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");
        weatherIcon.setTypeface(weatherFont);
        taskLoadUp("Vienna, AT");

       /* FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

    // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("HOME", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("HOME", "Error adding document", e);
                    }
                });*/
        return view;
    }

    public void taskLoadUp(String query) {
        if (OpenWeatherAPI.isNetworkAvailable(getContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query);
        } else {
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }
    private void selectTimeOfJoining(final DoggoEvent item) {
        //areYouSure(item);
        pickTime(item);
    }

    private void pickTime(final DoggoEvent item) {

        final int minute0 = item.getTime().getMinute();
        final int hour0 = item.getTime().getHour();

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                //Call Firestore to save event
                createEvent(item, selectedHour, selectedMinute);
                Toast.makeText(getContext(),"Scheduled walk at " + selectedHour + ":" + (selectedMinute < 10? "0"+selectedMinute:selectedMinute),Toast.LENGTH_LONG).show();
            }
        }, hour0, minute0, true);//Yes 24 hour time
        mTimePicker.setMessage("When are you joining " + item.getCreator().getName() +"?");
        mTimePicker.show();
    }

    private void createEvent(DoggoEvent item, int selectedHour, int selectedMinute) {
      //  item.getZone().setId("h8xuuF7wRpHSxnCFAftQ");
        final DoggoEvent event = new DoggoEvent();
        LocalDateTime originalEventTime = item.getTime();
        event.setTime(LocalDateTime.of(originalEventTime.getYear(), originalEventTime.getMonth(), originalEventTime.getDayOfMonth(), selectedHour,selectedMinute));
        event.setZone(item.getZone());
        ProfileFirestoreCallback profileFirestoreCallback = new ProfileFirestoreCallback() {
            @Override
            public void onDataRetrieved(Doggo doggo) {
                event.setCreator(doggo);
                //TODO
                //create event in FS
                Log.i("HomeFragment", "the new event to be created: " + event.getTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
                profileViewModel.addEvent(event);
            }

            @Override
            public void onDataRetrieved(ArrayList<DoggoEvent> events) {

            }

            @Override
            public void onDataRetrievedFollowings(ArrayList<Doggo> myFollowings) {

            }

            @Override
            public void onDataRetrievedFollowers(ArrayList<Doggo> myFollowers) {

            }
        };
        profileViewModel.getActiveDoggo(profileFirestoreCallback);
    }

    private List<DoggoEvent> createEventList(int size) {

        List<DoggoEvent> result = new ArrayList<DoggoEvent>();
        for (int i=0; i < ((MainActivity)getActivity()).getActiveDoggoEvents().size(); i++) {

            DoggoEvent event = ((MainActivity)getActivity()).getActiveDoggoEvents().get(i);
            event.setDoggosJoining(((MainActivity)getActivity()).getDefaultSearch());
            result.add(event);

        }

        return result;
    }
    class DownloadWeather extends AsyncTask< String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // loader.setVisibility(View.VISIBLE);

        }
        protected String doInBackground(String...args) {
            String xml = OpenWeatherAPI.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {

            try {
                if(xml!=null) {
                    JSONObject json = new JSONObject(xml);
                    if (json != null) {
                        JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                        JSONObject main = json.getJSONObject("main");
                        DateFormat df = DateFormat.getDateTimeInstance();

                        gowalk.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
                        weather.setText(String.format("%.2f", main.getDouble("temp")) + "°");
                        weatherIcon.setText(Html.fromHtml(OpenWeatherAPI.setWeatherIcon(details.getInt("id"),
                                json.getJSONObject("sys").getLong("sunrise") * 1000,
                                json.getJSONObject("sys").getLong("sunset") * 1000)));


                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }


        }



    }

    private void areYouSure(DoggoEvent item) {
        // Setting Alert Dialog Title
      /*  AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Your Doggo " + item.getCreator().getName() +" scheduled the walk for " + item.getTime().format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) +". " +
                "\nOthers are joining: \n" +item.joiningDoggosToString());
        alertDialogBuilder.setTitle("Wanna go for a walk? ");

        // Icon Of Alert Dialog
        //alertDialogBuilder.setIcon(R.drawable.question);
        // Setting Alert Dialog Message
        //alertDialogBuilder.setMessage("Are you sure,You want to exit");
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                ((MainActivity)getActivity()).getActiveDog().getEvents().add(item);
                Toast.makeText(getContext(), "You joined for the walk!", Toast.LENGTH_LONG).show();

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getContext(), "Walk rejected!", Toast.LENGTH_LONG).show();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();*/
    }

}
