/**
 * Copyright 2011 The Open Source Research Group,
 *                University of Erlangen-Nürnberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sweble.wikitext.example;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sweble.wikitext.articlecruncher.utils.SpeedMeter;

import de.fau.cs.osr.utils.BinaryPrefix;
import de.fau.cs.osr.utils.StringTools;

final class Gui
		implements
			Runnable
{
	private static final float UPDATES_PER_SECOND = 25;

	private static final int UPDATE_INTERVAL = (int) (1000 / UPDATES_PER_SECOND);

	private static final Logger logger = LoggerFactory.getLogger(Gui.class);

	// =====================================================================

	private final SpeedMeter speedMeter;

	private final DumpCruncher dumpCruncher;

	private Container pane;

	private JDialog dialog;

	private JPanel nexusPanel;

	private JProgressBar inTray;

	private JProgressBar outTray;

	private JPanel dbReaderPanel;

	private JLabel pagesRetrieved;

	private JLabel speed;

	private JLabel textRead;

	/*
	private JLabel waitingOnDbLabel;
	*/

	private JProgressBar processorsActive;

	/*
	private JProgressBar storersActive;
	*/

	private Timer timer;

	// =====================================================================

	public Gui(final DumpCruncher dumpCruncher)
	{
		this.dumpCruncher = dumpCruncher;

		this.speedMeter = new SpeedMeter(
				1.0f,
				UPDATE_INTERVAL / 1000.f,
				30.0f);

		this.lastUpdate = System.currentTimeMillis();

		javax.swing.SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
					{
						if ("Nimbus".equals(info.getName()))
						{
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				}
				catch (ClassNotFoundException ex)
				{
					logger.error(null, ex);
				}
				catch (InstantiationException ex)
				{
					logger.warn(null, ex);
				}
				catch (IllegalAccessException ex)
				{
					logger.warn(null, ex);
				}
				catch (javax.swing.UnsupportedLookAndFeelException ex)
				{
					logger.warn(null, ex);
				}

				dialog = new JDialog(new JFrame(), true);
				dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

				pane = dialog.getContentPane();

				pane.setLayout(
						new BoxLayout(pane, BoxLayout.Y_AXIS));

				GridBagConstraints gridBagConstraints;

				// Initialize empty fields to this to make dialog.pack() choose
				// a good width for the dialog.
				final String space = StringTools.strrep('_', 20);

				{
					final int inTrayCapacity = dumpCruncher.getOptions().value(
							"Nexus.InTrayCapacity",
							int.class);

					final int outTrayCapacity = dumpCruncher.getOptions().value(
							"Nexus.OutTrayCapacity",
							int.class);

					JLabel inTrayLabel;
					JLabel outTrayLabel;

					nexusPanel = new JPanel();
					inTrayLabel = new JLabel();
					inTray = new JProgressBar(0, inTrayCapacity);
					outTrayLabel = new JLabel();
					outTray = new JProgressBar(0, outTrayCapacity);

					nexusPanel.setBorder(BorderFactory.createTitledBorder("Nexus"));
					GridBagLayout nexusPanelLayout = new GridBagLayout();
					nexusPanelLayout.columnWeights = new double[] { 0.1, 1.0 };
					nexusPanel.setLayout(nexusPanelLayout);

					inTrayLabel.setText("in Tray: ");
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					nexusPanel.add(inTrayLabel, gridBagConstraints);

					inTray.setStringPainted(true);
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					nexusPanel.add(inTray, gridBagConstraints);

					outTrayLabel.setText("out Tray: ");
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.gridx = 0;
					gridBagConstraints.gridy = 1;
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					nexusPanel.add(outTrayLabel, gridBagConstraints);

					outTray.setStringPainted(true);
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.gridx = 1;
					gridBagConstraints.gridy = 1;
					gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					nexusPanel.add(outTray, gridBagConstraints);

					pane.add(nexusPanel);
				}

				{
					JLabel pagesRetrievedLabel;
					JLabel speedLabel;
					JLabel textReadLabel;

					dbReaderPanel = new JPanel();
					pagesRetrievedLabel = new JLabel();
					textReadLabel = new JLabel();
					pagesRetrieved = new JLabel();
					textRead = new JLabel();
					speedLabel = new JLabel();
					speed = new JLabel();
					/*
					waitingOnDbLabel = new JLabel();
					*/

					dbReaderPanel.setBorder(BorderFactory.createTitledBorder("DumpReaderJobGenerator"));
					dbReaderPanel.setName("Test");
					GridBagLayout dbReaderPanelLayout = new GridBagLayout();
					dbReaderPanelLayout.columnWidths = new int[] { 0, 10, 0, 10, 0, 10, 0 };
					dbReaderPanelLayout.rowHeights = new int[] { 0, 10, 0 };
					dbReaderPanelLayout.columnWeights = new double[] { 0.2, 1.0, 0.2, 1.0 };
					dbReaderPanelLayout.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
					dbReaderPanel.setLayout(dbReaderPanelLayout);

					pagesRetrievedLabel.setText("Pages retrieved:");
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.gridx = 0;
					gridBagConstraints.gridy = 0;
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					dbReaderPanel.add(pagesRetrievedLabel, gridBagConstraints);

					textReadLabel.setText("Bytes of text read:");
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.gridx = 0;
					gridBagConstraints.gridy = 2;
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					dbReaderPanel.add(textReadLabel, gridBagConstraints);

					pagesRetrieved.setText(space);
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.gridx = 2;
					gridBagConstraints.gridy = 0;
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					dbReaderPanel.add(pagesRetrieved, gridBagConstraints);

					textRead.setText(space);
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.gridx = 2;
					gridBagConstraints.gridy = 2;
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					dbReaderPanel.add(textRead, gridBagConstraints);

					speedLabel.setText("Speed:");
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.gridx = 4;
					gridBagConstraints.gridy = 0;
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					dbReaderPanel.add(speedLabel, gridBagConstraints);

					speed.setText(space);
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.gridx = 6;
					gridBagConstraints.gridy = 0;
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					dbReaderPanel.add(speed, gridBagConstraints);

					/*
					waitingOnDbLabel.setForeground(new Color(255, 102, 51));
					waitingOnDbLabel.setText("Waiting for DB");
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.gridx = 4;
					gridBagConstraints.gridy = 2;
					gridBagConstraints.gridwidth = 3;
					dbReaderPanel.add(waitingOnDbLabel, gridBagConstraints);
					*/

					pane.add(dbReaderPanel);
				}

				{
					final int numWorkers = dumpCruncher.getOptions().value(
							"processing-workers",
							int.class);

					JPanel processorPanel;
					JLabel processorsActiveLabel;

					processorPanel = new JPanel();
					processorsActiveLabel = new JLabel();
					processorsActive = new JProgressBar(0, numWorkers);

					processorPanel.setBorder(BorderFactory.createTitledBorder("Processors"));
					GridBagLayout processorsPanelLayout = new GridBagLayout();
					processorsPanelLayout.columnWeights = new double[] { 0.1, 1.0 };
					processorPanel.setLayout(processorsPanelLayout);

					processorsActiveLabel.setText("active Processors: ");
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					processorPanel.add(processorsActiveLabel, gridBagConstraints);

					processorsActive.setStringPainted(true);
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					processorPanel.add(processorsActive, gridBagConstraints);

					pane.add(processorPanel);
				}

				/*
				{
					final int numWorkers = dumpCruncher.getOptions().value(
							"store-workers",
							int.class);
					
					JPanel storerPanel;
					JLabel storersActiveLabel;
					
					storerPanel = new JPanel();
					storersActiveLabel = new JLabel();
					storersActive = new JProgressBar(0, numWorkers);
					
					storerPanel.setBorder(BorderFactory.createTitledBorder("Storers"));
					GridBagLayout storerPanelLayout = new GridBagLayout();
					storerPanelLayout.columnWeights = new double[] { 0.1, 1.0 };
					storerPanel.setLayout(storerPanelLayout);
					
					storersActiveLabel.setText("active Storers: ");
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					storerPanel.add(storersActiveLabel, gridBagConstraints);
					
					storersActive.setStringPainted(true);
					gridBagConstraints = new GridBagConstraints();
					gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
					gridBagConstraints.anchor = GridBagConstraints.EAST;
					storerPanel.add(storersActive, gridBagConstraints);
					
					pane.add(storerPanel);
				}
				*/

				// --

				timer = new Timer(UPDATE_INTERVAL, new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						if (dirty.get())
							SwingUtilities.invokeLater(Gui.this);
					}
				});

				timer.setCoalesce(true);
				timer.setRepeats(true);
				timer.start();

				// --

				dialog.pack();

				// --

				logger.info("Gui setup finished.");

				// this blocks!
				dialog.setVisible(true);
			}
		});
	}

	public void close()
	{
		if (timer != null)
			timer.stop();

		if (dialog != null)
			dialog.dispose();
	}

	// =====================================================================

	private long lastUpdate;

	private final AtomicBoolean dirty = new AtomicBoolean(true);

	private int pageCount;

	private long bytesRead;

	/*
	private boolean waitingOnDb;
	*/

	private final AtomicInteger inProcessing = new AtomicInteger(0);

	/*
	private final AtomicInteger inStoring = new AtomicInteger(0);
	*/

	// =====================================================================

	public synchronized void redrawLater()
	{
		dirty.set(true);
		long time = System.currentTimeMillis();
		if (time - lastUpdate > UPDATE_INTERVAL)
		{
			SwingUtilities.invokeLater(this);
			lastUpdate = time;
		}
	}

	public synchronized void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
	}

	public synchronized void setBytesRead(long bytesRead)
	{
		this.bytesRead = bytesRead;
	}

	/*
	public synchronized void setWaitingOnDb(boolean waitingOnDb)
	{
		this.waitingOnDb = waitingOnDb;
	}
	*/

	public synchronized void processingStarted()
	{
		inProcessing.incrementAndGet();
	}

	public synchronized void processingFinished()
	{
		inProcessing.decrementAndGet();
	}

	/*
	public synchronized void storingStarted()
	{
		inStoring.incrementAndGet();
	}
	
	public synchronized void storingFinished()
	{
		inStoring.decrementAndGet();
	}
	*/

	// =====================================================================

	@Override
	public void run()
	{
		dirty.set(false);

		{
			inTray.setValue(dumpCruncher.getNexus().getInTray().size());
			outTray.setValue(dumpCruncher.getNexus().getOutTray().size());
		}

		{
			pagesRetrieved.setText(String.valueOf(pageCount));

			BinaryPrefix bytesReadP = new BinaryPrefix(bytesRead);
			textRead.setText(String.format(
					"%d %s",
					bytesReadP.getValue(),
					bytesReadP.makePaddedUnit("B")));

			speedMeter.update(bytesRead, -1);
			float speed = speedMeter.getAvgSpeed();

			BinaryPrefix p = new BinaryPrefix((long) speed);
			this.speed.setText(String.format(
					"%7.2f %s",
					speed / p.getFactor(),
					p.makePaddedUnit("B/s")));

			/*
			this.waitingOnDbLabel.setVisible(waitingOnDb);
			*/
		}

		{
			this.processorsActive.setValue(inProcessing.get());
		}

		/*
		{
			this.storersActive.setValue(inStoring.get());
		}
		*/
	}
}
