//Manages text-to-speech
package org.gearticks;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class GearTicksTextToSpeech {
	private final TextToSpeech tts;
	private boolean ready;

	public GearTicksTextToSpeech(Context context) {
		this.tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
			public void onInit(int status) {
				GearTicksTextToSpeech.this.ready = true;
			}
		});
		this.tts.setLanguage(Locale.US);
		this.ready = false;
	}

	//Queues the text to be spoken
	@SuppressWarnings("deprecation")
	public void speak(String text) {
		this.tts.speak(text, TextToSpeech.QUEUE_ADD, null);
	}
	//Returns whether it is ready (seems to return true a bit prematurely)
	public boolean isReady() {
		return this.ready;
	}
}