package org.gearticks.opmodes.test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.gearticks.GearTicksTextToSpeech;
import org.gearticks.opmodes.BaseOpMode;

@Autonomous(name = "TTS", group = "test")
@Disabled
public class TextToSpeechTest extends BaseOpMode {
	private static final String[] WORDS = new String[]{
		"gear",
		"ticks",
		"dingus",
		"Schrodingus",
		"sneaky beaky",
		"we are not sexist on this team",
		"optimized",
		"Seize the tail of youth!",
		"manget",
		"contribooture",
		"sauce me the wrench, gucci"
	};
	private static final double DELAY_SECONDS = 1.5;

	private int wordIndex;
	private ElapsedTime wordTimer;
	private GearTicksTextToSpeech tts;

	protected void matchStart() {
		this.wordIndex = 0;
		this.wordTimer = new ElapsedTime();
		this.tts = new GearTicksTextToSpeech(this.hardwareMap.appContext);
	}
	protected void loopAfterStart() {
		final boolean ready = this.tts.isReady();
		this.telemetry.addData("Ready", ready);
		if (ready && this.wordTimer.seconds() > DELAY_SECONDS) {
			this.tts.speak(WORDS[wordIndex]);
			this.wordIndex = (this.wordIndex + 1) % WORDS.length;
			this.wordTimer.reset();
		}
	}
}