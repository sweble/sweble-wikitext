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

package org.sweble.wikitext.articlecruncher.utils;

import de.fau.cs.osr.utils.RingBuffer;

public class SpeedMeter
{
	private final RingBuffer<Float> speedBuffer;

	private final float progressUpdateInterval;

	private final float speedUpdateInterval;

	private long lastProgressUpdate = System.currentTimeMillis();

	private long lastSpeedUpdate = lastProgressUpdate;

	private long lastBytesRead = 0;

	private float currentProgress;

	private float currentSpeed = Float.NaN;

	private float avgSpeed = Float.NaN;

	private float eta = Float.POSITIVE_INFINITY;

	// =========================================================================

	public SpeedMeter(
			float progressUpdateIntervalInSeconds,
			float speedUpdateIntervalInSeconds,
			float etaWindowLengthInSeconds)
	{
		this.progressUpdateInterval = progressUpdateIntervalInSeconds;
		this.speedUpdateInterval = speedUpdateIntervalInSeconds;

		int capacity = (int) (etaWindowLengthInSeconds / speedUpdateInterval);
		if (capacity <= 0 || capacity > 4096)
			throw new IllegalArgumentException("Cannot realize ETA configuration");

		this.speedBuffer = new RingBuffer<Float>(
				capacity);
	}

	// =========================================================================

	public void update(long bytesRead, long fileLength)
	{
		long thisTime = System.currentTimeMillis();

		updateSpeed(thisTime, bytesRead, fileLength);
		updateProgress(thisTime, bytesRead, fileLength);
	}

	public float getCurrentProgress()
	{
		return currentProgress;
	}

	public float getCurrentSpeed()
	{
		return currentSpeed;
	}

	public float getAvgSpeed()
	{
		return avgSpeed;
	}

	public float getEta()
	{
		return eta;
	}

	// =========================================================================

	private void updateProgress(long now, long bytesRead, long fileLength)
	{
		float elapsed = (now - lastProgressUpdate) / 1000.f;
		if (elapsed > progressUpdateInterval)
		{
			lastProgressUpdate = now;

			currentProgress = (float) bytesRead / fileLength * 100.0f;
		}
	}

	private void updateSpeed(long now, long bytesRead, long fileLength)
	{
		float elapsed = (now - lastSpeedUpdate) / 1000.f;
		if (elapsed > speedUpdateInterval)
		{
			lastSpeedUpdate = now;

			long processed = bytesRead - lastBytesRead;
			lastBytesRead = bytesRead;

			currentSpeed = processed / elapsed;

			speedBuffer.add(currentSpeed);

			avgSpeed = 0;
			for (float speed : speedBuffer)
				avgSpeed += speed;

			avgSpeed /= speedBuffer.size();

			if (speedBuffer.size() > 10)
				eta = (fileLength - bytesRead) / avgSpeed / 60.0f;
		}
	}
}
