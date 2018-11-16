package silent;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpinnerListModel;
import javax.swing.JCheckBox;

//problems: doesn't work if sampleset on an object was manually set to sth different
//fix: ignore spinners (currently seen as circles)
//repeat arrows???
//make timestamp clickable?

public class MutedObjectChecker {

	private JFrame frmSilentHitsoundChecker;
	private JTextField txtDropFile;
	private JButton btnCheck;
	private JSpinner spinnerVolume;
	private JTextArea outputPane;
	private JScrollPane scrollOutput;
	private JLabel lblMutedVolume;
	private JSpinner spinnerMutedSet;
	private JLabel lblMutedSampleset;
	private JSpinner spinnerMutedType;

	private int mutedVolume = 5;
	private int mutedSet = 0;
	int mutedTypeInt = 2;
	private String mappath;
	private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss:SSS");

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MutedObjectChecker window = new MutedObjectChecker();
					window.frmSilentHitsoundChecker.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MutedObjectChecker() {
		initialize();
		addHandlers();
	}

	public void initialize() {
		frmSilentHitsoundChecker = new JFrame();
		frmSilentHitsoundChecker.setResizable(false);
		frmSilentHitsoundChecker.setTitle("Muted Object Checker");
		frmSilentHitsoundChecker.setBounds(100, 100, 450, 461);
		frmSilentHitsoundChecker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frmSilentHitsoundChecker.getContentPane().setLayout(springLayout);

		txtDropFile = new JTextField();
		springLayout.putConstraint(SpringLayout.NORTH, txtDropFile, 10, SpringLayout.NORTH,
				frmSilentHitsoundChecker.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, txtDropFile, 10, SpringLayout.WEST,
				frmSilentHitsoundChecker.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, txtDropFile, -373, SpringLayout.SOUTH,
				frmSilentHitsoundChecker.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, txtDropFile, -10, SpringLayout.EAST,
				frmSilentHitsoundChecker.getContentPane());
		txtDropFile.setText("Drop file here");
		txtDropFile.setEditable(false);
		frmSilentHitsoundChecker.getContentPane().add(txtDropFile);
		txtDropFile.setColumns(10);

		btnCheck = new JButton("Check");
		btnCheck.setEnabled(false);
		springLayout.putConstraint(SpringLayout.SOUTH, btnCheck, -10, SpringLayout.SOUTH,
				frmSilentHitsoundChecker.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnCheck, -10, SpringLayout.EAST,
				frmSilentHitsoundChecker.getContentPane());
		frmSilentHitsoundChecker.getContentPane().add(btnCheck);

		spinnerVolume = new JSpinner();
		springLayout.putConstraint(SpringLayout.WEST, spinnerVolume, 10, SpringLayout.WEST,
				frmSilentHitsoundChecker.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, spinnerVolume, -352, SpringLayout.EAST,
				frmSilentHitsoundChecker.getContentPane());
		spinnerVolume.setModel(new SpinnerNumberModel(5, 0, 100, 1));
		frmSilentHitsoundChecker.getContentPane().add(spinnerVolume);
		scrollOutput = new JScrollPane();
		springLayout.putConstraint(SpringLayout.NORTH, scrollOutput, 6, SpringLayout.SOUTH, txtDropFile);
		springLayout.putConstraint(SpringLayout.WEST, scrollOutput, 10, SpringLayout.WEST,
				frmSilentHitsoundChecker.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollOutput, -6, SpringLayout.NORTH, spinnerVolume);
		springLayout.putConstraint(SpringLayout.EAST, scrollOutput, -10, SpringLayout.EAST,
				frmSilentHitsoundChecker.getContentPane());

		frmSilentHitsoundChecker.getContentPane().add(scrollOutput);

		outputPane = new JTextArea();
		scrollOutput.setViewportView(outputPane);
		outputPane.setEditable(false);

		lblMutedVolume = new JLabel("Muted Volume");
		springLayout.putConstraint(SpringLayout.NORTH, lblMutedVolume, 3, SpringLayout.NORTH, spinnerVolume);
		springLayout.putConstraint(SpringLayout.WEST, lblMutedVolume, 6, SpringLayout.EAST, spinnerVolume);
		frmSilentHitsoundChecker.getContentPane().add(lblMutedVolume);

		spinnerMutedSet = new JSpinner();
		springLayout.putConstraint(SpringLayout.SOUTH, spinnerVolume, -6, SpringLayout.NORTH, spinnerMutedSet);
		springLayout.putConstraint(SpringLayout.NORTH, spinnerMutedSet, 1, SpringLayout.NORTH, btnCheck);
		spinnerMutedSet.setModel(new SpinnerNumberModel(0, 0, 100, 1));
		frmSilentHitsoundChecker.getContentPane().add(spinnerMutedSet);

		lblMutedSampleset = new JLabel("Muted Sampleset");
		springLayout.putConstraint(SpringLayout.EAST, spinnerMutedSet, -4, SpringLayout.WEST, lblMutedSampleset);
		springLayout.putConstraint(SpringLayout.NORTH, lblMutedSampleset, 4, SpringLayout.NORTH, btnCheck);
		springLayout.putConstraint(SpringLayout.WEST, lblMutedSampleset, 0, SpringLayout.WEST, lblMutedVolume);
		frmSilentHitsoundChecker.getContentPane().add(lblMutedSampleset);

		spinnerMutedType = new JSpinner();
		springLayout.putConstraint(SpringLayout.WEST, spinnerMutedSet, 6, SpringLayout.EAST, spinnerMutedType);
		springLayout.putConstraint(SpringLayout.EAST, spinnerMutedType, -395, SpringLayout.EAST,
				frmSilentHitsoundChecker.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, spinnerMutedType, 1, SpringLayout.NORTH, btnCheck);
		springLayout.putConstraint(SpringLayout.WEST, spinnerMutedType, 10, SpringLayout.WEST,
				frmSilentHitsoundChecker.getContentPane());
		spinnerMutedType.setModel(new SpinnerListModel(new String[] { "S", "N", "D" }));
		frmSilentHitsoundChecker.getContentPane().add(spinnerMutedType);
	}

	private void check() {

		outputPane.setText("");
		ArrayList<TimingPoint> timingPointList = new ArrayList<TimingPoint>();
		ArrayList<Integer> circleOffsetList = new ArrayList<Integer>();
		ArrayList<Integer> silentCircleList = new ArrayList<Integer>();
		ArrayList<Integer> sliderOffsetList = new ArrayList<Integer>();
		ArrayList<Integer> silentSliderList = new ArrayList<Integer>();
		File fileName = new File(mappath);
		String line = "";
		int sectionCount = 1;
		double sliderMultiplier = 1;

		try {

			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			double currentBeat = 0;
			int currentArrayIndex = 0;

			while (((line = bufferedReader.readLine()) != null)) {
				if (line.equals("")) {
					sectionCount += 1;
					continue;
				}
				if (line.contains("[")) {
					continue;
				}
				
				if (sectionCount == 5) {
					if (line.split(":")[0].equals("SliderMultiplier")) {
						sliderMultiplier = Double.parseDouble(line.split(":")[1]);
					}
				}

				if (sectionCount == 7) {
					int timingOffset = Integer.parseInt(line.split(",")[0]);
					int volume = Integer.parseInt(line.split(",")[5]);
					double svMult = 1.0;
					if (mutedSet > 0 && Integer.parseInt(line.split(",")[3]) == mutedTypeInt
							&& Integer.parseInt(line.split(",")[4]) == mutedSet) {
						volume = 0;
					}

					if (Integer.parseInt(line.split(",")[6]) == 1) {
						currentBeat = Double.parseDouble(line.split(",")[1]);
					} else {
						svMult = (-100) / (Double.parseDouble(line.split(",")[1]));
					}

					
					timingPointList.add(new TimingPoint(timingOffset - 4, volume, svMult, currentBeat));
					timingPointList.add(new TimingPoint(timingOffset - 3, volume, svMult, currentBeat));
					timingPointList.add(new TimingPoint(timingOffset - 2, volume, svMult, currentBeat));
					timingPointList.add(new TimingPoint(timingOffset - 1, volume, svMult, currentBeat));
					
					timingPointList.add(new TimingPoint(timingOffset, volume, svMult, currentBeat));

				}

				if (sectionCount == 10) {
					if (!line.contains("|")) {
						if (line.split(",").length > 6) //does something to ignore slider idk wtf kibb did tbh
							continue;
						
						if (Integer.parseInt(line.split(":")[3]) != 0) {
							if (Integer.parseInt(line.split(":")[3]) < 6) {
								int circleOffset = Integer.parseInt(line.split(",")[2]);
								String formatted = sdf.format(new Date(circleOffset)) + " -";
								outputPane.append("Silent circle at " + formatted + "\n");
							}
						} else {
							int circleOffset = Integer.parseInt(line.split(",")[2]);
							circleOffsetList.add(circleOffset);
						}

					} else {
						int sliderOffset = Integer.parseInt(line.split(",")[2]);

						for (int i = currentArrayIndex; i < timingPointList.size(); i++) {
							 if (i == timingPointList.size() - 1) {
	                                if (timingPointList.get(i).offset <= sliderOffset) {
	                                    currentArrayIndex = i;
	                                    break;
	                                }
	                            }
	                            else {
	                                if (timingPointList.get(i).offset <= sliderOffset
	                                        && timingPointList.get(i + 1).offset >= sliderOffset) {
	                                    currentArrayIndex = i;
	                                    break;
	                                }
	                            }
						}
						
						int sliderDuration = (int) ((Double.parseDouble(line.split(",")[7]) / (100 * sliderMultiplier * timingPointList.get(currentArrayIndex).sv )) * timingPointList.get(currentArrayIndex).beat) + 1;
						
						for (int i = 0; i < Integer.parseInt(line.split(",")[6]); i++) {
							sliderOffsetList.add(sliderOffset + sliderDuration*i);
						}

					}
				}
			}

			bufferedReader.close();

			int currentPointOffset = 0;
			int nextPointOffset = 0;

			for (int x = 0; x < timingPointList.size(); x++) {

				if (x != timingPointList.size() - 1) {
					currentPointOffset = timingPointList.get(x).offset;
					nextPointOffset = timingPointList.get(x + 1).offset;
				} else {
					currentPointOffset = timingPointList.get(x).offset;
					nextPointOffset = Integer.MAX_VALUE;
				}
				timingPointList.get(x).ending = nextPointOffset;
			}

			for (int circleOffset : circleOffsetList) {

				for (TimingPoint timingPoint : timingPointList) {

					if (timingPoint.volume <= mutedVolume) {
						if (circleOffset >= timingPoint.offset && circleOffset < timingPoint.ending) {
							silentCircleList.add(circleOffset);
						}
					}

				}

			}
			for (int sliderOffset : sliderOffsetList) {

				for (TimingPoint timingPoint : timingPointList) {

					if (timingPoint.volume <= mutedVolume) {
						if (sliderOffset >= timingPoint.offset && sliderOffset < timingPoint.ending) {
							silentSliderList.add(sliderOffset);
						}
					}

				}

			}
			for (int circleOffset : silentCircleList) {
				String formatted = sdf.format(new Date(circleOffset))  + " -";
				outputPane.append("Silent circle at " + formatted + "\n");
			}
			for (int sliderOffset : silentSliderList) {
				String formatted = sdf.format(new Date(sliderOffset))  + " -";
				outputPane.append("Silent slider at " + formatted + "\n");
			}

			if (silentCircleList.size() == 0 && silentSliderList.size() == 0) {
				outputPane.append("No Silent Circles found");
			}
 
		} catch (FileNotFoundException ex1) {

			outputPane.append("Error with file/calculation" + "\n");

		}

		catch (IOException ex) {

			outputPane.append("File can't be read" + "\n");

		}

	}

	private void addHandlers() {
		DropTarget d = new DropTarget() {
			private static final long serialVersionUID = 1L;

			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable()
							.getTransferData(DataFlavor.javaFileListFlavor);
					mappath = droppedFiles.get(0).getPath();
					txtDropFile.setText(mappath);
					String pog = mappath.substring(mappath.length() - 4, mappath.length());
					if (pog.equalsIgnoreCase(".osu")) {
						btnCheck.setEnabled(true);
					} else {
						btnCheck.setEnabled(false);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		txtDropFile.setDropTarget(d);

		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				check();
			}
		});

		spinnerVolume.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mutedVolume = (int) spinnerVolume.getValue();
			}
		});

		spinnerMutedSet.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mutedSet = (int) spinnerMutedSet.getValue();
			}
		});

		spinnerMutedSet.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				mutedSet = (int) spinnerMutedSet.getValue();
			}
		});

	}
}
