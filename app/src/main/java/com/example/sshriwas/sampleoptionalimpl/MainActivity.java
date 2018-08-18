package com.example.sshriwas.sampleoptionalimpl;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Optional;

/**
 * The purpose of this code is to demonstrate the use of Java 8's Optional class
 * as a wrapper around objects. Using this wrapper we can check for null pointer
 * exception and make our code more readable.
 * Reference urls:
 * http://www.oracle.com/technetwork/articles/java/java8-optional-2175753.html
 * https://www.baeldung.com/java-optional
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //2. Now suppose we need to fetch version of some USB. Then this would be the
        //earlier approach
        new Computer().getmSoundCard().getmUsb().getVersion();

        //but the above statement may throw null pointer exception at various places.
        //Such as if the computer doesn't have a sound card or the sound card doesn't have USB
        //and so on.
        // To mitigate this we earlier checked null condition using if-statement as follows-
        Computer mComputer = new Computer();
        if (mComputer != null){
            Soundcard mSoundCard = mComputer.getmSoundCard();
            if (mSoundCard != null){
                USB mUSB = mSoundCard.getmUsb();
                if(mUSB != null){
                    String version = mUSB.getVersion();
                    Log.v("TAG", ""+version);
                }
            }
        }
        //As it can be seen that above code contains so many if conditons. This makes the code
        //bulky and difficult to read. Also the code is still prone to further null pointer exceptions
        //if and where the user may have forgotten to add an if-statement. So its not completely
        //secure. And its not easy to read either.

        /*
        Java SE 8 introduces a new class called java.util.Optional<T> that is inspired from the ideas
        of Haskell and Scala. It is a class that encapsulates an optional value.
        You can view Optional as a single-value container that either contains a value or
        doesn't (it is then said to be "empty"),
         */

        //3. Creating Optional objects
        //First this is how to create an empty Optional:
        Optional<Soundcard> optionalEmptySoundcard = Optional.empty();

        //Optional with non-null value -
        //If soundcard were null, a NullPointerException would be immediately thrown
        // (rather than getting a latent error once you try to access properties of the soundcard).
        Soundcard soundcard = new Soundcard("");
        Optional<Soundcard> optionalSoundcard = Optional.of(soundcard);

        //Also by using ofNullable you can create a Optional object which can hold null value.
        Optional<Soundcard> optionalSoundcard2 = Optional.ofNullable(soundcard);
        //Above, if soundcard is null then ofNullable will not throw NullPointerException
        // instead it will return an empty optional object.

        //4. Do something if value is present - If you wished to check whether soundcard is not null
        //before calling its getUsb() then you would ideally do it as follows .
        if (optionalSoundcard2!=null){
            System.out.print(optionalSoundcard.get());
        }

        //However this can also be achieved using isPresent()
        if (optionalSoundcard2.isPresent()){
            System.out.print(optionalSoundcard.get());
        }

        //or.... using ifPresent(Consumer<T> consumer)
        // - just to summarize, consumer is a functional interface
        //whose method takes a parameter as an argument but does
        //not return anything.
        //something like: optionalSoundcard2.ifPresent((b)->System.out.println());
        //but again, since here the lambda expression is simply calling the method
        //system.out.println() so we replace it with a method reference as follows-
        optionalSoundcard2.ifPresent(System.out::println);

        //4. Returning default if result of an operation is null
        //say for eg.
        Soundcard mySoundCard = new Soundcard("My Soundcard");
        //Here we are providing a default sound card if mySoundCard is null
        Soundcard MyOptionalSoundCard = mySoundCard != null ? mySoundCard : new Soundcard("Default soundcard");

        //This can be implemented using Optional using orElse() -
        Optional<Soundcard> myOptionalSoundCard = Optional.ofNullable(mySoundCard);
        //Below if myOptionalSoundCard is null then orElse will come into effect
        Soundcard MyOptionalSoundCard2 = myOptionalSoundCard.orElse(new Soundcard("Default Soundcard"));
        //There is also orElseThrow(Supplier<T> exceptionSupplier) which will throw an exception if optional instance is null
        //Note: Supplier is another functional interface that accepts no input parameter but returns a value. It is used for
        //lazy generation of values.
        Soundcard MyOptionalSoundCard3 = myOptionalSoundCard.orElseThrow(()-> new IllegalStateException());

        //Using Optional.filter() to check some property. Filter takes a predicate as an argument
        //a predicate is a functional interface whose method accepts a parameter and returns a boolean
        // For eg you want to check if USB
        // version is a particular version.

        //The usual way to do this would be to check if USB instance is not null and then verify the
        //version number
        USB usb = new USB();
        if (usb != null && usb.getVersion().equalsIgnoreCase("3.0")){
            System.out.println("Ok");

        }

        //This can be re-written using filter() of Optional class as follows -
        Optional<USB> optionalUsb = Optional.ofNullable(usb);
        optionalUsb.filter(usbparam -> usbparam.getVersion().equalsIgnoreCase("3.0"))
                .ifPresent((name) -> System.out.println("ok"));


    }
}

//1. We begin by creating a class Computer. This has a Soundcard which has a USB with a version
class Computer{
    private Soundcard mSoundCard;

    public Soundcard getmSoundCard() {
        return mSoundCard;
    }

    public void setmSoundCard(Soundcard mSoundCard) {
        this.mSoundCard = mSoundCard;
    }
}

class Soundcard{
    private USB mUsb;
    String desc;

    public Soundcard(String description){
        this.desc = description;
    }

    public USB getmUsb() {
        return mUsb;
    }

    public void setmUsb(USB mUsb) {
        this.mUsb = mUsb;
    }
}

class USB{
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
