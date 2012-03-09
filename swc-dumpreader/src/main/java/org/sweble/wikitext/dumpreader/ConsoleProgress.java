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

package org.sweble.wikitext.dumpreader;

import de.fau.cs.osr.utils.BinaryPrefix;
import de.fau.cs.osr.utils.RingBuffer;

public class ConsoleProgress
{
	private final RingBuffer<Float> speedBuffer = new RingBuffer<Float>(30);
	
	// =========================================================================
	
	private long lastTime = System.currentTimeMillis();
	
	private long lastSpeedTime = lastTime;
	
	private long lastPointer = 0;
	
	private float progress;
	
	private float curSpeed = Float.NaN;
	
	private float eta = Float.POSITIVE_INFINITY;
	
	// =========================================================================
	
	public void reportProgress(
			long filePointer,
			long fileLength,
			long inWork,
			long pageCount)
	{
		long thisTime = System.currentTimeMillis();
		
		updateSpeed(thisTime, filePointer, fileLength);
		
		report(thisTime, filePointer, fileLength, inWork, pageCount);
	}
	
	private void update(float elapsed, long filePointer, long fileLength)
	{
		progress = (float) filePointer / fileLength * 100.0f;
	}
	
	private void updateSpeed(long thisTime, long filePointer, long fileLength)
	{
		float elapsed = (thisTime - lastSpeedTime) / 1000.f;
		if (elapsed > 1.f)
		{
			lastSpeedTime = thisTime;
			
			long processed = filePointer - lastPointer;
			lastPointer = filePointer;
			
			curSpeed = processed / elapsed;
			
			speedBuffer.add(curSpeed);
			
			float avgSpeed = 0;
			for (float speed : speedBuffer)
				avgSpeed += speed;
			
			avgSpeed /= speedBuffer.size();
			
			if (speedBuffer.size() > 10)
				eta = (fileLength - filePointer) / avgSpeed / 60.0f;
		}
	}
	
	private void report(
			long thisTime,
			long filePointer,
			long fileLength,
			long inWork,
			long pageCount)
	{
		float elapsed = (thisTime - lastTime) / 1000.f;
		if (elapsed > 0.1f)
		{
			lastTime = thisTime;
			
			update(elapsed, filePointer, fileLength);
			
			StringBuilder out = new StringBuilder("\r");
			
			buildReportString(out, filePointer, inWork, pageCount);
			
			System.out.print(out.toString());
		}
	}
	
	private String buildReportString(
			StringBuilder out,
			long filePointer,
			long inWork,
			long pageCount)
	{
		{
			out.append("Queue: ");
			out.append(String.format("%3d", inWork));
		}
		// --------
		{
			out.append(", Page count: ");
			out.append(String.format("%10d", pageCount));
		}
		// --------
		{
			BinaryPrefix p = new BinaryPrefix(filePointer);
			out.append(", ");
			out.append("Processed: ");
			out.append(String.format("%4d", p.getValue()));
			out.append(" ");
			out.append(p.makePaddedUnit("B"));
		}
		// --------
		{
			out.append(", ");
			out.append("Progress: ");
			out.append(String.format("%5.2f%%", progress));
		}
		// --------
		{
			out.append(", ");
			out.append("Speed: ");
			if (Float.isNaN(curSpeed))
			{
				out.append(" ------   B/s");
			}
			else
			{
				BinaryPrefix p = new BinaryPrefix((long) curSpeed);
				out.append(String.format("%7.2f", curSpeed / p.getFactor()));
				out.append(" ");
				out.append(p.makePaddedUnit("B/s"));
			}
		}
		// --------
		{
			out.append(", ");
			out.append("ETA: ");
			if (Float.isInfinite(eta))
			{
				out.append("  ----  min");
			}
			else
			{
				out.append(String.format("%6.2f", eta));
				out.append(" min");
			}
		}
		// --------
		return out.toString();
	}
}
