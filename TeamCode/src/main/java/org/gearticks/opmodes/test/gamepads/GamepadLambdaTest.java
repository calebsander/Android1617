package org.gearticks.opmodes.test.gamepads;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import org.gearticks.GamepadWrapper;
import org.gearticks.components.generic.OpModeContext;
import org.gearticks.components.generic.component.OpModeComponent;
import org.gearticks.components.generic.component.OpModeComponent.DefaultTransition;
import org.gearticks.components.generic.component.OpModeComponentAbstract;
import org.gearticks.components.generic.statemachine.NetworkedStateMachine;
import org.gearticks.components.velocity.opmode.generic.VelocityBaseOpMode;
import org.gearticks.dimsensors.DimLed;
import org.gearticks.hardware.configurations.VelocityConfiguration;

@TeleOp
@Disabled
public class GamepadLambdaTest extends VelocityBaseOpMode {
	private static class LedStateComponent extends OpModeComponentAbstract<DefaultTransition> {
		private final boolean ledState;
		private final DeviceInterfaceModule dim;
		private final GamepadWrapper gamepad;

		public LedStateComponent(boolean ledState, OpModeContext<VelocityConfiguration> opModeContext) {
			super(DefaultTransition.class);
			this.ledState = ledState;
			this.dim = opModeContext.configuration.dim;
			this.gamepad = opModeContext.gamepads[0];
		}

		public void setup() {
			super.setup();
			this.dim.setLED(DimLed.BLUE.id, this.ledState);
		}
		public DefaultTransition run() {
			final DefaultTransition superTransition = super.run();
			if (superTransition != null) return superTransition;

			if (this.gamepad.newly(g -> g.getLeftY() > 0.5)) return DefaultTransition.DEFAULT;
			else return null;
		}
	}

	public OpModeComponent<?> getComponent(OpModeContext<VelocityConfiguration> opModeContext) {
		final LedStateComponent offComponent = new LedStateComponent(false, opModeContext);
		final LedStateComponent onComponent = new LedStateComponent(true, opModeContext);
		final NetworkedStateMachine component = new NetworkedStateMachine();
		component.setInitialComponent(offComponent);
		component.addConnection(offComponent, DefaultTransition.DEFAULT, onComponent);
		component.addConnection(onComponent, DefaultTransition.DEFAULT, offComponent);
		return component;
	}
	public boolean isV2() {
		return false;
	}
}