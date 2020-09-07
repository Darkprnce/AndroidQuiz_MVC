package com.crypto.quizapp.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.crypto.quizapp.Beans.QuestionsBean;
import com.crypto.quizapp.Databases.DatabaseClient;
import com.crypto.quizapp.Fragments.AboutUsFrag;
import com.crypto.quizapp.Fragments.TopicSelectFrag;
import com.crypto.quizapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.crypto.quizapp.helper.CommonSharedPreference;
import com.google.android.material.navigation.NavigationView;
import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.FragNavTransactionOptions;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;  // Navigation drawer
    private ActionBarDrawerToggle drawerToggle;
    private FragNavController fragNavController;   // FragNav library controller
    private FragNavTransactionOptions fragNavTransactionOptions; // Fragnav transaction option

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        // declaring fragnav controller
        fragNavController = new FragNavController(getSupportFragmentManager(), R.id.content_frame);

        // declaring fragnav transaction options
        fragNavTransactionOptions = new FragNavTransactionOptions.Builder().customAnimations(R.anim.slide_in_from_right,
                R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right).build();

        // adding root fragments
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(TopicSelectFrag.newInstance());

        // setting root fragments to fragnav controller
        fragNavController.setRootFragments(fragments);

        // setting fragnav transaction option to fragnav controller
        fragNavController.setDefaultTransactionOptions(fragNavTransactionOptions);

        // setting first fragment to display when we get on Homeactivity
        fragNavController.initialize(FragNavController.TAB1, savedInstanceState);

        //navigation drawer listener to navigate through activity and fragment from drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:

                        // checking if the fragment is root fragment or not, if not then pop the fragment from stack,
                        // if it is root fragment then switch the fragment to First fragment in stack

                        if(!fragNavController.isRootFragment()){
                         fragNavController.popFragment();
                        }else {
                            fragNavController.switchTab(FragNavController.TAB1);
                        }
                        break;

                    case R.id.nav_aboutus:
                        // push fragment in the stack
                        fragNavController.pushFragment(AboutUsFrag.newInstance());
                        break;

                    case R.id.nav_reset:
                        // clearing user answers
                        deleteQuestions();
                        break;

                    case R.id.nav_add:
                        // directing to addquestionActivity
                        Intent z = new Intent(HomeActivity.this, AddQuestionActivity.class);
                        startActivity(z);
                        break;

                    case R.id.nav_logout:
                        // logging out from the app, changing shared preference value will allow to go to signinactivity from Splashactivity
                        CommonSharedPreference.setsharedText(HomeActivity.this, "logged", null);
                        Intent i = new Intent(HomeActivity.this, SignInActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clearing the history stack
                        startActivity(i);
                        finish(); // finish the activity so we won't return to this activity by back press
                        break;


                }
                drawer.closeDrawer(GravityCompat.START); // close drawer after selecting an option
                return true;
            }
        });

        // if the user gets on this screen for first time, then add the demo questions to the database
        if (TextUtils.isEmpty(CommonSharedPreference.getsharedText(HomeActivity.this, "firstTime"))) {
            addquestions();
            CommonSharedPreference.setsharedText(HomeActivity.this, "firstTime", "no");
        }
    }


    private void addquestions() {

        // adding demo questions to the database
        // look in QuestionsBean Class for the detail about the parameters

        class AddQuestions extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                //  Adding some questions

                List<QuestionsBean> questionlist = new ArrayList<>();
                questionlist.add(new QuestionsBean("androidcore", 1, "no", "1", "what is android?", "O.S", "Software", "ninja", "samurai", "O.S", ""));
                questionlist.add(new QuestionsBean("androidcore", 1, "no", "2", "Once installed on a device, each Android application lives in_______?", "device Memory", "external Memory", "security sandbox", "None of the above", "security sandbox", ""));
                questionlist.add(new QuestionsBean("androidcore", 1, "no", "3", "What are direct Subclasses of Activity?", "AccountAuthenticatorActivity", "ActivityGroup", "ExpandableListActivity", "all of the above", "all of the above", ""));
                questionlist.add(new QuestionsBean("androidcore", 1, "no", "4", "Which component is not activated by an Intent?", "Activity", "Services", "Content Provider", "Broadcast Receiver", "ContentProvider", ""));
                questionlist.add(new QuestionsBean("androidcore", 1, "no", "5", "Parent class of Service?", "object", "Context", "ContextWrapper", "ContxtThemeWrapper", "ContextWrapper", ""));
                questionlist.add(new QuestionsBean("androidcore", 1, "no", "6", "When contentProvider would be activated?", "using Intent", "using SQLite", "using ContentResolver", "None of the above", "using ContentResolver", ""));
                questionlist.add(new QuestionsBean("androidcore", 1, "no", "7", "Which are the screen sizes in Android?", "small", "normal", "large", "all of the above", "all of the above", ""));
                questionlist.add(new QuestionsBean("androidcore", 1, "no", "8", "You can shut down an activity by calling its _______ method", "onDestory()", "finishActivity()", "finish()", "None of the above", "finish()", ""));
                questionlist.add(new QuestionsBean("androidcore", 1, "no", "9", "Which one is NOT related to fragment class?", "DialogFragment", "ListFragment", "PreferenceFragment", "CursorFragment", "CursorFragment", ""));
                questionlist.add(new QuestionsBean("androidcore", 1, "no", "10", "Definition of Loader?", "loaders does not make it easy to asynchronously load data in an activity or fragment.", "loaders make it easy to synchronously load data in an activity or fragment.", "loaders make it easy to asynchronously load data in an activity or fragment.", "None of the above.", "loaders make it easy to asynchronously load data in an activity or fragment.", ""));
                questionlist.add(new QuestionsBean("androidcore", 2, "no", "1", "A type of service provided by android that shows messages and alerts to user is", "Content Providers", "View System", "Notifications Manager", "Activity Manager.", "Notifications Manager", ""));
                questionlist.add(new QuestionsBean("androidcore", 2, "no", "2", "Broadcast that includes information about battery state, level, etc. is", "android.intent.action.BATTERY_CHANGED", "android.intent.action.BATTERY_LOW", "android.intent.action.BATTERY_OKAY", "android.intent.action.CALL_BUTTON", "android.intent.action.BATTERY_CHANGED", ""));
                questionlist.add(new QuestionsBean("androidcore", 2, "no", "3", "One of application component, that manages application's background services is called", "Activities", "Broadcast Receivers", "Services", "Content Providers", "Services", ""));
                questionlist.add(new QuestionsBean("androidcore", 2, "no", "4", "In android studio, callback that is called when activity interaction with user is started is", "onStart", "onStop", "onResume", "onDestroy", "onResume", ""));
                questionlist.add(new QuestionsBean("androidcore", 2, "no", "5", "Tab that can be used to do any task that can be done from DOS window is", "TODO", "messages", "terminal", "comments.", "terminal", ""));
                questionlist.add(new QuestionsBean("androidcore", 2, "no", "6", "Space outside of widget can be customized using", "padding", "height", "weight", "margins", "margins", ""));
                questionlist.add(new QuestionsBean("androidcore", 2, "no", "7", "Tab that shows hierarchy of project is", "Build variants", "Structure", "Favorites", "Project", "Structure", ""));
                questionlist.add(new QuestionsBean("androidcore", 2, "no", "8", "In android, compiled code is executed by part of android system called", "DEX", "DVM", "JDK", "API", "DVM", ""));
                questionlist.add(new QuestionsBean("androidcore", 2, "no", "9", "A part of android studio, that work as a simulator for android devices is called", "driver", "emulator", "stub", "firmware", "emulator", ""));
                questionlist.add(new QuestionsBean("androidcore", 2, "no", "10", "Code that provide easy way to use all android features is called", "API", "DEX", "DVM", "JDK", "API", ""));
                questionlist.add(new QuestionsBean("androidcore", 3, "no", "1", "If a button is clicked whose method in Java is not defined then android application will", "do nothing", "crash", "show black screen", "get hang", "crash", ""));
                questionlist.add(new QuestionsBean("androidcore", 3, "no", "2", "Android content provider architecture, component that does not resides on data layer is", "Internet", "SQLite", "content provider", "Files", "content provider", ""));
                questionlist.add(new QuestionsBean("androidcore", 3, "no", "3", "Android calculates pixels density of mobile (currently using app) using unit of measurement", "CP", "SP", "DP", "GP", "DP", ""));
                questionlist.add(new QuestionsBean("androidcore", 3, "no", "4", "In android studio, each new activity created must be defined in", "res/layout", "res/values", "Build.gradle", "AndroidManifest.xml", "AndroidManifest.xml", ""));
                questionlist.add(new QuestionsBean("androidcore", 3, "no", "5", "Android component that controls external elements of file is called", "intent", "resource", "view", "manifest", "resource", ""));
                questionlist.add(new QuestionsBean("androidcore", 3, "no", "6", " A type of service provided by android that controls application lifespan and activity pile is", "Activity Manager", "View System", "Notifications Manager", "Content Providers", "Activity Manager", ""));
                questionlist.add(new QuestionsBean("androidcore", 3, "no", "7", "Activity is needed to be stopped by doing any operation until resumed callback is called, type of callback is", "onPause", "onDestroy", "onResume", "onRestart", "onPause", ""));
                questionlist.add(new QuestionsBean("androidcore", 3, "no", "8", "One of operating system that cannot be used for android application development is", "Eclipse", "Cooja", "Netbeans", "Android studio", "Cooja", ""));
                questionlist.add(new QuestionsBean("androidcore", 3, "no", "9", "In android studio, a service that is started and cannot be stopped even calling activity is destroyed is", "startService()", "onStop()", "onDestroy()", "bindService()", "bindService()", ""));
                questionlist.add(new QuestionsBean("androidcore", 3, "no", "10", "Android understandable form of code is called", "JDK", "DEX", "DVM", "API", "DEX", ""));
                questionlist.add(new QuestionsBean("androidcore", 4, "no", "1", "One of operating system that cannot be used for android application development is", "Windows", "Mac", "Linux", "Contiki", "Contiki", ""));
                questionlist.add(new QuestionsBean("androidcore", 4, "no", "2", "A class that allows to display messages on logcat windows is", "Toast Class", "Log class", "makeTest class", "Show class", "Log class", ""));
                questionlist.add(new QuestionsBean("androidcore", 4, "no", "3", "One of recent android version is", "Kitkat", "Marshmallow", "Lolipop", "Jelly Bean", "Marshmallow", ""));
                questionlist.add(new QuestionsBean("androidcore", 4, "no", "4", "Required Java environment for development is called", "JDK", "DEX", "DVM", "API", "JDK", ""));
                questionlist.add(new QuestionsBean("androidcore", 4, "no", "5", "In android studio, virtual console type tab that is used as command line interface is called", "memory", "CPU", "logcat", "ADB logs", "ADB logs", ""));
                questionlist.add(new QuestionsBean("androidcore", 4, "no", "6", "BroadcastReceiver's subclass overrides method", "OnCreate()", "onStart()", "onRestart()", "onReceive()", "onReceive()", ""));
                questionlist.add(new QuestionsBean("androidcore", 4, "no", "7", "Dalvik Virtual Machine (DVM) actually uses core features of", "Windows", "Mac", "Linux", "Contiki", "Linux", ""));
                questionlist.add(new QuestionsBean("androidcore", 4, "no", "8", "On implementing method of onStartCommand(), service must be stopped after task is completed using", "stopSelf()", "stopService()", "stopSelf() or stopService()", "endService()", "stopSelf() or stopService()", ""));
                questionlist.add(new QuestionsBean("androidcore", 4, "no", "9", "Space between edge to of widget content of widget can be customized using", "margins", "height", "padding", "weight", "padding", ""));
                questionlist.add(new QuestionsBean("androidcore", 4, "no", "10", "Button option can be choose from palette category", "textfields", "containers", "widgets", "layouts", "widgets", ""));
                questionlist.add(new QuestionsBean("androidadvance", 1, "no", "1", "In android studio, tab in which error is shown is called", "logcat", "memory", "ADB logs", "CPU", "logcat", ""));
                questionlist.add(new QuestionsBean("androidadvance", 1, "no", "2", "By default in android studio during app development, file that holds information about app's fundamental features and components is", "AndroidManifest.xml", "res/values", "Build.gradle", "res/layout", "AndroidManifest.xml", ""));
                questionlist.add(new QuestionsBean("androidadvance", 1, "no", "3", "Requests from Content Provider class is handled by method", "onCreate", "onSelect", "onClick", "ContentResolver", "ContentResolver", ""));
                questionlist.add(new QuestionsBean("androidadvance", 1, "no", "4", "Once broadcast after system boot finished is", "android.intent.action.BATTERY_OKAY", "android.intent.action.REBOOT", "android.intent.action.BOOT_COMPLETED", "android.intent.action.BUG_REPORT", "android.intent.action.BOOT_COMPLETED", ""));
                questionlist.add(new QuestionsBean("androidadvance", 1, "no", "5", "A type of class that will be responsible to design main screen activity on first time launch of application is called", "Activity class", "Parent class", "Child class", "Inherited class", "Activity class", ""));
                questionlist.add(new QuestionsBean("androidadvance", 1, "no", "6", "Method is called when service was created first-time using onStartCommand() or onBind() is", "onStart()", "onCreate()", "onRestart()", "startService()", "onCreate()", ""));
                questionlist.add(new QuestionsBean("androidadvance", 1, "no", "7", "Underlying operating system for android is", "Linux", "Windows", "Contiki", "Ubuntu", "Linux", ""));
                questionlist.add(new QuestionsBean("androidadvance", 1, "no", "8", "On implementing method of onBind(), service must provide an interface for user by returning object called", "Ibinder", "Intent", "R", "Layout", "Ibinder", ""));
                questionlist.add(new QuestionsBean("androidadvance", 1, "no", "9", "Android component that manages appearance and format on screen is called", "fragment", "intent", "view", "layout", "layout", ""));
                questionlist.add(new QuestionsBean("androidadvance", 1, "no", "10", "In android UI, <code>onClick</code> is actually a", "class", "button", "property", "function", "property", ""));
                questionlist.add(new QuestionsBean("androidadvance", 2, "no", "1", "SendStickyBroadcast(Intent) method is used to show that Intent is", "prominent", "prioritized", "optional", "sticky", "sticky", ""));
                questionlist.add(new QuestionsBean("androidadvance", 2, "no", "2", "Required android environment for development is called", "SDK", "IDE", "APK", "JDK", "SDK", ""));
                questionlist.add(new QuestionsBean("androidadvance", 2, "no", "3", " In android studio, quick options can be accessed from", "tool bar", "menu bar", "navigation bar", "editor tab", "menu bar", ""));
                questionlist.add(new QuestionsBean("androidadvance", 2, "no", "4", "In android studio, tab in which CPU usage of app is shown is called", "ADB logs", "memory", "CPU", "logcat", "CPU", ""));
                questionlist.add(new QuestionsBean("androidadvance", 2, "no", "5", "A method that is used to handle what happens after clicking a button is", "OnCreate", "onSelect", "onClick", "onDo", "onClick", ""));
                questionlist.add(new QuestionsBean("androidadvance", 2, "no", "6", "Method that is used to produce log messages in android is", "Log.d()", "Log.D()", "Log.R()", "Log.r()", "Log.d()", ""));
                questionlist.add(new QuestionsBean("androidadvance", 2, "no", "7", "In android studio, main activity for application must be declared in", "<intent>", "<intent-filter>", "<intent-layout>", "<intent-activity>", "<intent-filter>", ""));
                questionlist.add(new QuestionsBean("androidadvance", 2, "no", "8", "One of application component, that controls UI and manage user interaction with phone screen is called", "Content Providers", "Activities", "Broadcast Receivers", "Services", "Activities", ""));
                questionlist.add(new QuestionsBean("androidadvance", 2, "no", "9", "A type of service provided by android that helps in creating user interfaces is", "Notifications Manager", "Content Providers", "Activity Manager", "View System", "View System", ""));
                questionlist.add(new QuestionsBean("androidadvance", 2, "no", "10", "Android component that works like database", "Services", "Activities", "Broadcast Receivers", "Content Providers", "Content Providers", ""));
                questionlist.add(new QuestionsBean("androidadvance", 3, "no", "1", "Multiple inheritance is not allowed in Java therefore in an android activity there cannot be more then one", "super class", "child class", "sub class", "public class", "super class", ""));
                questionlist.add(new QuestionsBean("androidadvance", 3, "no", "2", "By default in android studio during app development, directory made for xml files is", "res/layout", "res/values", "AndroidManifest.xml", "Build.gradle", "res/values", ""));
                questionlist.add(new QuestionsBean("androidadvance", 3, "no", "3", "A type of layout elements that allows all included elements in order is", "ConstraintLayout", "TextviewLayout", "LinearLayout", "RelativeLayout", "LinearLayout", ""));
                questionlist.add(new QuestionsBean("androidadvance", 3, "no", "4", "Tab that stores history of most visited places is", "Favorites", "Structure", "Build variants", "Project", "Favorites", ""));
                questionlist.add(new QuestionsBean("androidadvance", 3, "no", "5", "In android, functionality of one class can be used in other class by using option", "extends", "extended", "extending", "extend", "extends", ""));
                questionlist.add(new QuestionsBean("androidadvance", 3, "no", "6", "A class that displays a toast-shaped message for user is", "Log class", "makeTest class", "Show class", "Toast class", "Toast class", ""));
                questionlist.add(new QuestionsBean("androidadvance", 3, "no", "7", "One of application component, that manages database issues is called", "Services", "Content Providers", "Broadcast Receivers", "Activities", "Content Providers", ""));
                questionlist.add(new QuestionsBean("androidadvance", 3, "no", "8", "Android library that provides access to model of application is", "android.opengl", "android.content", "android.app", "android.database", "android.app", ""));
                questionlist.add(new QuestionsBean("androidadvance", 3, "no", "9", "In android emulator mode, left-hand side of smartphone area elements is called", "palette", "widgets", "design", "button", "palette", ""));
                questionlist.add(new QuestionsBean("androidadvance", 3, "no", "10", "Broadcast of device reboot is", "android.intent.action.BATTERY_LOW", "android.intent.action.REBOOT", "android.intent.action.BATTERY_CHANGED", "android.intent.action.CALL", "android.intent.action.REBOOT", ""));
                questionlist.add(new QuestionsBean("androidadvance", 4, "no", "1", "Android component that shows part of an activity on screen is called", "intent", "fragment", "view", "manifest", "fragment", ""));
                questionlist.add(new QuestionsBean("androidadvance", 4, "no", "2", "Layout or design of an android application is saved in", ".text file", ".java file", ".dex file", ".xml file", ".xml file", ""));
                questionlist.add(new QuestionsBean("androidadvance", 4, "no", "3", "For specialized layout elements, palette element used is", "custom", "textfields", "containers", "widgets", "custom", ""));
                questionlist.add(new QuestionsBean("androidadvance", 4, "no", "4", "Method that is called to clean up any resources used by services is", "stopSelf()", "onStop()", "onDestroy()", "stopService()", "onDestroy()", ""));
                questionlist.add(new QuestionsBean("androidadvance", 4, "no", "5", "UI design hierarchy and individual design elements can be seen using", "properties", "component tree", "layout", "terminal", "component tree", ""));
                questionlist.add(new QuestionsBean("androidadvance", 4, "no", "6", "Multiple inheritance concept is indirectly used by using", "public class", "child class", "sub class", "super class", "sub class", ""));
                questionlist.add(new QuestionsBean("androidadvance", 4, "no", "7", "Android library that provides access to graphics is", "android.content", "android.opengl", "android.app", "android.database", "android.opengl", ""));
                questionlist.add(new QuestionsBean("androidadvance", 4, "no", "8", "In android Studio, TODO tab is actually used for", "comments", "storage", "classes", "function", "comments", ""));
                questionlist.add(new QuestionsBean("androidadvance", 4, "no", "9", " In android, logcat is also referred as", "Verbos", "Info", "Console", "Method", "Console", ""));
                questionlist.add(new QuestionsBean("androidadvance", 4, "no", "10", "One of option that is not part of palette elements is", "layouts", "containers", "date and time", "default", "default", ""));
                questionlist.add(new QuestionsBean("javacore", 1, "no", "1", "what is java?", "programming language", "software", "Super Villain", "O.S", "programming language", ""));
                questionlist.add(new QuestionsBean("javacore", 1, "no", "2", "Which one of these lists contains only Java programming language keywords?", "class, if, void, long, Int, continue", "goto, instanceof, native, finally, default, throws", "try, virtual, throw, final, volatile, transient", "strictfp, constant, super, implements, do", "goto, instanceof, native, finally, default, throws", ""));
                questionlist.add(new QuestionsBean("javacore", 1, "no", "3", "What of the following is the default value of a local variable?", "null", "0", "Depends upon the type of variable", "Not assigned", "Not assigned", ""));
                questionlist.add(new QuestionsBean("javacore", 1, "no", "4", "What is the size of double variable?", "8 bit", "16 bit", "32 bit", "64 bit", "64 bit", ""));
                questionlist.add(new QuestionsBean("javacore", 1, "no", "5", "What is the default value of byte variable?", "0", "0.0", "null", "not defined", "0", ""));
                questionlist.add(new QuestionsBean("javacore", 1, "no", "6", "Which of the following stands true about default modifier of class members?", "By default, variables, methods and constructors can be accessed by subclass only.", "By default, variables, methods and constructors can be accessed by any class lying in any package.", "By default, variables, methods and constructors can be accessed by any class lying in the same package.", "None of the above.", "By default, variables, methods and constructors can be accessed by any class lying in the same package.", ""));
                questionlist.add(new QuestionsBean("javacore", 1, "no", "7", "What is inheritance?", "It is the process where one object acquires the properties of another.", "inheritance is the ability of an object to take on many forms.", "inheritance is a technique to define different methods of same type.", "None of the above.", "It is the process where one object acquires the properties of another.", ""));
                questionlist.add(new QuestionsBean("javacore", 1, "no", "8", "What is TreeSet Interface?", "It is a Set implemented when we want elements in a tree based order.", "It is a Set implemented when we want elements in a sorted order.", "It is a Set implemented when we want elements in a binary tree format.", "It is a Set implemented when we want elements in a hiearchical order.", "It is a Set implemented when we want elements in a sorted order.", ""));
                questionlist.add(new QuestionsBean("javacore", 1, "no", "9", "Dynamic binding uses which information for binding?", "type.", "object.", "Both of the above.", "None of the above.", "object.", ""));
                questionlist.add(new QuestionsBean("javacore", 1, "no", "10", "What happens when thread's yield() method is called?", "Thread returns to the ready state.", "Thread returns to the waiting state.", "Thread starts running.", "None of the above.", "Thread returns to the ready state.", ""));
                questionlist.add(new QuestionsBean("javacore", 2, "no", "1", "What is runtime polymorphism?", "Runtime polymorphism is a process in which a call to an overridden method is resolved at runtime rather than at compile-time.", "Runtime polymorphism is a process in which a call to an overloaded method is resolved at runtime rather than at compile-time.", "Both of the above.", "None of the above.", "Runtime polymorphism is a process in which a call to an overridden method is resolved at runtime rather than at compile-time.", ""));
                questionlist.add(new QuestionsBean("javacore", 2, "no", "2", "Which method must be implemented by all threads?", "wait()", "start()", "stop()", "run()", "run()", ""));
                questionlist.add(new QuestionsBean("javacore", 2, "no", "3", "Multiple inheritance means?", " one class inheriting from more super classes", "more classes inheriting from one super class", "more classes inheriting from more super classes", "None of the above", " one class inheriting from more super classes", ""));
                questionlist.add(new QuestionsBean("javacore", 2, "no", "4", "Which statement is not true in java language?", "A public member of a class can be accessed in all the packages.", "A private member of a class cannot be accessed by the methods of the same class.", "A private member of a class cannot be accessed from its derived class.", "A protected member of a class can be accessed from its derived class.", "A private member of a class cannot be accessed by the methods of the same class.", ""));
                questionlist.add(new QuestionsBean("javacore", 2, "no", "5", "To prevent any method from overriding, we declare the method as?", "static", "const", "final", "abstract", "final", ""));
                questionlist.add(new QuestionsBean("javacore", 2, "no", "6", "Which one of the following is not true?", "A class containing abstract methods is called an abstract class.", "Abstract methods should be implemented in the derived class.", "An abstract class cannot have non-abstract methods.", "A class must be qualified as ‘abstract’ class, if it contains one abstract method.", "An abstract class cannot have non-abstract methods.", ""));
                questionlist.add(new QuestionsBean("javacore", 2, "no", "7", "The fields in an interface are implicitly specified as?", "static only", "protected", "private", "both static and final", "both static and final", ""));
                questionlist.add(new QuestionsBean("javacore", 2, "no", "8", "Which of the following is not true?", "An interface can extend another interface.", "A class which is implementing an interface must implement all the methods of the interface.", "An interface can implement another interface.", "An interface is a solution for multiple inheritance in java.", "An interface can implement another interface.", ""));
                questionlist.add(new QuestionsBean("javacore", 2, "no", "9", "Which of the following is true?", "A finally block is executed before the catch block but after the try block.", " A finally block is executed, only after the catch block is executed.", "A finally block is executed whether an exception is thrown or not.", "A finally block is executed, only if an exception occurs.", "A finally block is executed whether an exception is thrown or not.", ""));
                questionlist.add(new QuestionsBean("javacore", 2, "no", "10", "Among these expressions, which is(are) of String type ?", "“0”", "“ab” + “cd”", " ‘0’", "Both (A) and (B) above", "Both (A) and (B) above", ""));
                questionlist.add(new QuestionsBean("javacore", 3, "no", "1", "What is the type and value of the following expression? (Notice the integer division)\n" + "-4 + 1/2 + 2*-3 + 5.0", "int -5", "double -4.5", "int -4", "double -5.0", "double -5.0", ""));
                questionlist.add(new QuestionsBean("javacore", 3, "no", "2", "What is printed by the following statement?\n" + "System.out.print(“Hello,\nworld!”);", "Hello, \nworld!", "Hello, world!", "Hello,\nworld!", "“Hello, \nworld!”", "Hello,\nworld!", ""));
                questionlist.add(new QuestionsBean("javacore", 3, "no", "3", "Which of the following variable declaration would NOT compile in a java program?", "int var;", " int VAR;", "int var1;", "int 1_var;", "int 1_var;", ""));
                questionlist.add(new QuestionsBean("javacore", 3, "no", "4", "Which of the following is TRUE?", "In java, an instance field declared public generates a compilation error.", "A class has always a constructor (possibly automatically supplied by the java compiler).", "int is the name of a class available in the package java.lang", "Instance variable names may only contain letters and digits.", "A class has always a constructor (possibly automatically supplied by the java compiler).", ""));
                questionlist.add(new QuestionsBean("javacore", 3, "no", "5", "A constructor____", "Must have the same name as the class it is declared within.", "Is used to create objects.", "May be declared private", "(a), (b) and (c) above.", "(a), (b) and (c) above.", ""));
                questionlist.add(new QuestionsBean("javacore", 3, "no", "6", "What is byte code in the context of Java?", "The type of code generated by a Java compiler.", "The type of code generated by a Java Virtual Machine.", "It is another name for a Java source file.", "It is the code written within the instance methods of a class.", "The type of code generated by a Java compiler.", ""));
                questionlist.add(new QuestionsBean("javacore", 3, "no", "7", "What is garbage collection in the context of Java?", "The operating system periodically deletes all the java files available on the system.", "Any package imported in a program and not used is automatically deleted.", "When all references to an object are gone, the memory used by the object is automatically reclaimed.", "The JVM checks the output of any Java program and deletes anything that doesn’t make sense.", "When all references to an object are gone, the memory used by the object is automatically reclaimed.", ""));
                questionlist.add(new QuestionsBean("javacore", 3, "no", "8", "The java run time system automatically calls this method while garbage collection.", "finalizer()", "finalize()", "finally()", "finalized()", "finalize()", ""));
                questionlist.add(new QuestionsBean("javacore", 3, "no", "9", "The correct order of the declarations in a Java program is?", "Package declaration, import statement, class declaration", "Import statement, package declaration, class declaration", "Import statement, class declaration, package declaration", "Class declaration, import statement, package declaration", "Package declaration, import statement, class declaration", ""));
                questionlist.add(new QuestionsBean("javacore", 3, "no", "10", "An overloaded method consists of?", "The same method name with different types of parameters", "The same method name with different number of parameters", "The same method name and same number and type of parameters with different return type", "Both (a) and (b) above", "Both (a) and (b) above", ""));
                questionlist.add(new QuestionsBean("javacore", 4, "no", "1", "A protected member cannot be accessed in?", "a subclass of the same package", "a non-subclass of the same package", "a non-subclass of different package", "a subclass of different package", "a non-subclass of different package", ""));
                questionlist.add(new QuestionsBean("javacore", 4, "no", "2", "All exception types are subclasses of the built-in class?", "Throwable", "Exception", "RuntimeException", "Error", "Throwable", ""));
                questionlist.add(new QuestionsBean("javacore", 4, "no", "3", "When an overridden method is called from within a subclass, it will always refer to the version of that method defined by the___", "Super class", "Subclass", "Compiler will choose randomly", "Interpreter will choose randomly", "Subclass", ""));
                questionlist.add(new QuestionsBean("javacore", 4, "no", "4", "Mark the incorrect statement from the following:", "Java is a fully object oriented language with strong support for proper software engineering techniques", "In java it is not easy to write C-like so called procedural programs", "In java language objects have to be manipulated", "In java language error processing is built into the language", "In java language error processing is built into the language", ""));
                questionlist.add(new QuestionsBean("javacore", 4, "no", "5", "In java, objects are passed as?", "Copy of that object", "Method called call by value", "Memory address", "Constructor", "Memory address", ""));
                questionlist.add(new QuestionsBean("javacore", 4, "no", "6", "Which of the following is not a component of Java Integrated Development Environment (IDE)?", "Net Beans", "Borland’s Jbuilder", "Symantec’s Visual Café", "Microsoft Visual Fox Pro", "Symantec’s Visual Café", ""));
                questionlist.add(new QuestionsBean("javacore", 4, "no", "7", "Identify, from among the following, the incorrect variable name(s).", "2ndName", "_theButton", "$reallyBigNumber", "CurrentWeatherStateofplanet", "2ndName", ""));
                questionlist.add(new QuestionsBean("javacore", 4, "no", "8", "What would the output be of the above Program – III before and after it is called?", "and b before call : 15 20 a and b after call : 30 10", "a and b before call : 5 2 a and b after call : 15 20", "a and b before call : 15 20 a and b after call : 15 20", "a and b before call : 30 10 a and b after call : 15 20", "a and b before call : 15 20 a and b after call : 15 20", ""));
                questionlist.add(new QuestionsBean("javacore", 4, "no", "9", "What would the argument passing method be which is used by the above Program – III?", "Call by value", "Call by reference", "Call by java.lang class", "Call by byte code", "Call by value", ""));
                questionlist.add(new QuestionsBean("javacore", 4, "no", "10", "Members of a class specified as ……………….. are accessible only to methods of that class.", "Protected", "Final", "Public", "Private", "Private", ""));
                questionlist.add(new QuestionsBean("javaadvance", 1, "no", "1", "Java compiler javac translates Java source code into_____", "Assembler language", "Byte code", "Bit code", "Machine code", "Byte code", ""));
                questionlist.add(new QuestionsBean("javaadvance", 1, "no", "2", "______are used to document a program and improve its readability.", "System cells", "Keywords", "Comments", "Control structures", "Comments", ""));
                questionlist.add(new QuestionsBean("javaadvance", 1, "no", "3", "In Java, a character constant’s value is its integer value in the______character set.", "EBCDIC", "Unicode", "ASCII", "Binary", "Unicode", ""));
                questionlist.add(new QuestionsBean("javaadvance", 1, "no", "4", "In Java, a try block should immediately be followed by one or more_______blocks.", "Throw", "Run", "Exit", "Catch", "Catch", ""));
                questionlist.add(new QuestionsBean("javaadvance", 1, "no", "5", "An abstract data type typically comprises a______and a set of_____respectively.", "Data representation, classes", "Database, operations", "Data representation, objects", "Data representation, operations.", "Data representation, operations.", ""));
                questionlist.add(new QuestionsBean("javaadvance", 1, "no", "6", "In object-oriented programming, the process by which one object acquires the properties of another object is called?", "Inheritance", "Encapsulation", "Polymorphism", "Overloading", "Inheritance", ""));
                questionlist.add(new QuestionsBean("javaadvance", 1, "no", "7", "Re-implementing an inherited method in a sub class to perform a different task from the parent class is called?", "Binding", "extending", "Transferring", "Hiding", "extending", ""));
                questionlist.add(new QuestionsBean("javaadvance", 1, "no", "8", "In a class definition, the special method provided to be called to create an instance of that class is known as a/an?", "Interpreter", "Destructor", "Constructor", "Object", "Constructor", ""));
                questionlist.add(new QuestionsBean("javaadvance", 1, "no", "9", "Which of the following statements about Java Threads is correct?", "Java threads don’t allow parts of a program to be executed in parallel", "Java is a single-threaded language", "Java’s garbage collector runs as a high priority thread", "Ready, running and sleeping are three states that a thread can be in during its life cycle", "Ready, running and sleeping are three states that a thread can be in during its life cycle", ""));
                questionlist.add(new QuestionsBean("javaadvance", 1, "no", "10", "When the ejbRemove method encounters a sysyem problem ,it should throw_________", "javax.ejb.NoSuchEntityException", "java.ejb.EJBException", "java.ejb.RemoveException", "javax.ejb.DuplicateKeyException", "java.ejb.EJBException", ""));
                questionlist.add(new QuestionsBean("javaadvance", 2, "no", "1", "___________ is a block of java code in JSP that is used define class-wide variables and methods in the generated class file", "scriplets", "declarations", "element", "expression", "declarations", ""));
                questionlist.add(new QuestionsBean("javaadvance", 2, "no", "2", "Which of the following is not an implicit object ?", "date", "request", "out", "pagecontext", "date", ""));
                questionlist.add(new QuestionsBean("javaadvance", 2, "no", "3", "An Enterprise JavaBeans can be deployed in _______", "J2EE server", "Weblogic", "Web sphere", "All of the above", "All of the above", ""));
                questionlist.add(new QuestionsBean("javaadvance", 2, "no", "4", "Mapping files (*.hbm.xml) is used __________", "to map persistent objects to a relational database", "to configure the hibernate services (connection driver class, connection URL)", "to configure the hibernate services (connection username, connection password, dialect etc)", "All the above", "to map persistent objects to a relational database", ""));
                questionlist.add(new QuestionsBean("javaadvance", 2, "no", "5", "Consider the following HTML page code: < html > < body > < a href='/servlet/HelloServlet' >POST< /a > < /body >< /html > Which method of HelloServIet will be invoked when the hyperlink is clicked?", "doGet", "doPost", "doHref", "servicePost", "doGet", ""));
                questionlist.add(new QuestionsBean("javaadvance", 2, "no", "6", "To determine the behaviour of the beans in an application , we make use of", "Java.beans.SimpleBeanInfo", "Java.beans.Introspector", "Java.awt.*", "None of the above", "Java.beans.Introspector", ""));
                questionlist.add(new QuestionsBean("javaadvance", 2, "no", "7", "Which of the elements defined within the taglib element of taglib descriptor file are required", "uri", "jsp-version", "display-name", "None", "jsp-version", ""));
                questionlist.add(new QuestionsBean("javaadvance", 2, "no", "8", "Name the element within the tag element that defines the tag class that implements the functionality of tag", "tag", "tag-class", "tag-name", "tag-uri", "tag-class", ""));
                questionlist.add(new QuestionsBean("javaadvance", 2, "no", "9", "__________ provides the ability to directly insert java into an HTML document", "declarations", "scriptlets", "directives", "None of the above", "scriptlets", ""));
                questionlist.add(new QuestionsBean("javaadvance", 2, "no", "10", "___________ pattern enables improved network traffic and response time. Fewer remote calls are made and less data is passed back and forth", "Value Objects", "Data Access Objects", "Facade", "Business Delegate", "Value Objects", ""));
                questionlist.add(new QuestionsBean("javaadvance", 3, "no", "1", "Which of the following of beans would survive a server crash?", "Stateless session beans", "Stateful session beans", "Message-driven beans", "Entity beans", "Entity beans", ""));
                questionlist.add(new QuestionsBean("javaadvance", 3, "no", "2", "Which of the following statements are true about locating or using the home interface of a session bean?", "Once acquired, the home interface can be used only once", "Each instance of a session bean has its own EJBHome object", "The InitialContext must be narrowed before it can be used to get the home interface", "None of the above", "None of the above", ""));
                questionlist.add(new QuestionsBean("javaadvance", 3, "no", "3", "Which of the following statements are correct about a session bean whose class contains the following method? public void ejbCreate (String id)", "It is a Stateless session bean", "The home interface of the bean has the method create (String id) declared in it", "The component interface ofthe bean has the method ejbCreate (String id) declared in it", "None of the above", "The home interface of the bean has the method create (String id) declared in it", ""));
                questionlist.add(new QuestionsBean("javaadvance", 3, "no", "4", "What are valid methods for HttpSessionListener interface?", "sessionRemoved", "sessionDestroyed", "sessionReCreated", "sessionReplaced", "sessionDestroyed", ""));
                questionlist.add(new QuestionsBean("javaadvance", 3, "no", "5", "Sites using HTTPS that is HTTP plus SSL(Secure Sockets Layer ) can be identified by", "There is no way one can detect that site uses HTTPS protocol", "The URL of the website begins with https: instead of http", "The URL of the website begins with ssl: instead of http", "The URL of the website begins with shttp", "The URL of the website begins with https: instead of http", ""));
                questionlist.add(new QuestionsBean("javaadvance", 3, "no", "6", "An object which implements the interfaces java.rmi.Remote and java.io.Serializable is being sent as a method parameter from one JVM to another. How would it be sent by RMI?", "RMI will serialize the object and send it", "RMI will send the stub of the object", "Either A or B Throws an exception", "None", "RMI will send the stub of the object", ""));
                questionlist.add(new QuestionsBean("javaadvance", 3, "no", "7", "In the JMS, message producers and message consumers are created by which of the following objects?", "Connection Factories", "Message Listeners", "Connections", "Sessions", "Sessions", ""));
                questionlist.add(new QuestionsBean("javaadvance", 3, "no", "8", "To author a Session bean which of the following classes are needed?", "A Home interface, A Remote Interface, a class that implements Enterprisebean interface and a PrimaryKey class", "A Home interface, A Remote Interface and a class that implements the SessionBean interface", "A Remote Interface and a class that implements the SessionBean interface", "A Home interface, A Remote Interface and a class that implements the EnterpriseBean interface", "A Home interface, A Remote Interface and a class that implements the SessionBean interface", ""));
                questionlist.add(new QuestionsBean("javaadvance", 3, "no", "9", "What will happen if a transactional client invokes an enterprise Bean method whose transaction attribute is set to `Never` within a transaction context ?", "TransactionRequiredException is thrown to the client", "TransactionNotSupportedException is thrown to the client", "Transaction is suspended till the bean method is completed", "RemoteException is thrown to the client", "RemoteException is thrown to the client", ""));
                questionlist.add(new QuestionsBean("javaadvance", 3, "no", "10", "Which of the following statement is false regarding the exceptions in JDBC", "SQLWarning objects are a subclass of SQLException that deal with database access warnings", "Warnings stop the execution of an application, as exceptions do; they simply alert the user that something did not happen as planned", "Connection object has a getWarning() method in it", "Statement and ResultSet objects have getWarning() methods in it", "Warnings stop the execution of an application, as exceptions do; they simply alert the user that something did not happen as planned", ""));
                questionlist.add(new QuestionsBean("javaadvance", 4, "no", "1", "JDBC-ODBC Bridge does not work with Microsoft J++, because it does not support", "Java Native Interface", "JNDI", "JINI", "None of above", "Java Native Interface", ""));
                questionlist.add(new QuestionsBean("javaadvance", 4, "no", "2", "The JDBC-ODBC bridge is", "Multithreaded", "Singlethreaded", "Both of the above", "None of the above", "Multithreaded", ""));
                questionlist.add(new QuestionsBean("javaadvance", 4, "no", "3", "Name the class that includes the getSession method that is used to get the HttpSession object", "HttpServletResponse", "SessionContext", "SessionConfig", "HttpServletRequest", "HttpServletRequest", ""));
                questionlist.add(new QuestionsBean("javaadvance", 4, "no", "4", "The EJB timer service is used for timing notifications. It can be used with", "CMP entity beans", "both BMP and CMP entity beans", "message-driven beans", "B and C", "message-driven beans", ""));
                questionlist.add(new QuestionsBean("javaadvance", 4, "no", "5", "What does the EJB specification architecture define", "Transactional components", "Distributed object components", "Server-side components", "All of the above", "All of the above", ""));
                questionlist.add(new QuestionsBean("javaadvance", 4, "no", "6", "How can I use JDBC to create a database?", "Include create=true at end of JDBC URL", "Execute 'CREATE DATABASE jGuru' SQL statement", "Execute 'STRSQL' and 'CREATE COLLECTION jGuru' SQL statements", "Database creation is DBMS specific", "Database creation is DBMS specific", ""));
                questionlist.add(new QuestionsBean("javaadvance", 4, "no", "7", "When using a JavaBean to get all the parameters from a form, what must the property be set to (??? in the following code) for automatic initialization? < jsp:useBean id='fBean' class='govi.FormBean' scope='request'/> < jsp:setProperty name='fBean' property='???' /> < jsp:forward page='/servlet/JSP2Servlet' />", "*", "all", "@", "=", "*", ""));
                questionlist.add(new QuestionsBean("javaadvance", 4, "no", "8", "Stub is a ____________ proxy for the remote object.", "Server side", "Client side", "Both side", "none of the above", "Client side", ""));
                questionlist.add(new QuestionsBean("javaadvance", 4, "no", "9", "Which page directive attribute allows you to take care of possible thread conflicts?", "session", "extends", "buffer", "IsThreadSafe", "IsThreadSafe", ""));
                questionlist.add(new QuestionsBean("javaadvance", 4, "no", "10", "In the Model View Controller architecture of an enterprise application, which of the following can be 'best suited' as the Controller?", "Servlets", "Java Server Page", "Session Bean", "Option 1 and Option 3", "Option 1 and Option 3", ""));


                //adding to database
                for (int i = 0; i < questionlist.size(); i++) {
                    DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                            .questionDao()
                            .insert(questionlist.get(i));
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        AddQuestions addQuestions = new AddQuestions();
        addQuestions.execute();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {

        // if the drawer is open then close it
        // if the fragment is not the root fragment then pop the fragment from stack
        // if the fragment is root fragment then onBackpressed called

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(!fragNavController.isRootFragment()){
                fragNavController.popFragment();
            }else {
                super.onBackPressed();
            }
        }
    }


    private void deleteQuestions() {

        // deleting the user answer from the database

        class DeleteQues extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(HomeActivity.this).getAppDatabase()
                        .questionDao()
                        .deleteAllQuestions();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(HomeActivity.this, "All data is reset", Toast.LENGTH_SHORT).show();
                addquestions(); // again adding demo questions because all values were erased from the database
                fragNavController.replaceFragment(TopicSelectFrag.newInstance()); // this will refresh the TopicSelectFragment
            }
        }

        DeleteQues dt = new DeleteQues();
        dt.execute();

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        fragNavController.onSaveInstanceState(outState);
    }
}
