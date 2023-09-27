package Presentation;

import Business.Car;

/**
 * 
 * @author IwanB
 * 
 * Used to notify any interested object that implements this interface
 * and registers with InstructionListPanel of an InstructionSelection
 *
 */
public interface ICarSelectionNotifiable {
	public void carSelected(Car car);
}
