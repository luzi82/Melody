package com.luzi82.melody;

public class AudioFileCheck {

	public static class Result {
		boolean mIsAudio;
		Type mType;
	}

	public static Result check(String mFilePath) {
		Result ret = new Result();
		ret.mIsAudio = true;
		if (mFilePath.toLowerCase().endsWith(".wav")) {
			ret.mType=Type.WAV;
		} else {
			ret.mIsAudio = false;
		}
		return ret;
	}

	public enum Type {
		WAV, MP3, OGG, OTHER
	}

	private AudioFileCheck() {
	}

}
