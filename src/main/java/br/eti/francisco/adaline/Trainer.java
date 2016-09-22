package br.eti.francisco.adaline;

import java.util.ArrayList;
import java.util.List;

import br.eti.francisco.plot.Function;
import br.eti.francisco.plot.Plot;

public class Trainer {

	private List<Sample> sample = new ArrayList<Sample>();
	
	private Adaline adaline;
	
	public Trainer() {

	}
	
	public Trainer(double initWeight[], double bias) {
		if(adaline == null){
			this.adaline = new Adaline(initWeight.length);
		}
		for (int i = 0; i < initWeight.length; i++) {
			this.adaline.getWeight()[i] = initWeight[i];
		}
		this.adaline.setBias(bias);
	}
	
	public void addSample(double [] input, double output){
		if(adaline == null){
			this.adaline = new Adaline(input.length);
		}
		Sample s = new Sample();
		s.input = input;
		s.output = output;
		sample.add(s);
	}

	public void plot(String label){
		Plot plot = new Plot();
		plot.plotFunction(10, -3, 3, -3, 3, new Function() {
			
			@Override
			public double calculateY(double x) {
				return (-x * adaline.getWeight()[0] - adaline.getBias()) / adaline.getWeight()[1];
			}
		});

		for(Sample s : sample){
			plot.filledCircle(s.input[0], s.input[1], 0.05, s.output == 1 ? Plot.GREEN : Plot.RED);
		}
		
		plot.text(1, 1.5, label);
		
	}
	
	
	public int train(double rate, int max){
		try {
			for (int i = 0; i < max; i++) {
				double eqm = 0;
				if(i % 30 == 0){
//					plot("Time: " + i);
				}
				for(Sample s : sample){
					double e = Math.abs(adaline.train(s.input, s.output, rate));
					System.out.println(e);
					eqm += e * e;
				}
				eqm /= sample.size();
				System.out.println(eqm);
				if(eqm < 0.000001){
					return i;
				}
			}
			return max;
		} finally {
			plot("Final");

		}
	}

	public Adaline getAdaline() {
		return adaline;
	}

	private class Sample{
		private double [] input;
		private double output;
	}
}
