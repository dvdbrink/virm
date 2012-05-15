package nl.clockwork.android.virm.scanner;

import nl.clockwork.android.virm.history.History;
import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable, History.Item {
	private String title;
	private long scanTime;

	public Result(String title, long scanTime) {
		this.title = title;
		this.scanTime = scanTime;
	}

	@Override
	public long getId() {
		return title.hashCode();
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	@Override
	public long getScanTime() {
		return this.scanTime;
	}
	
	////////////////////
	///// Parceble /////
	////////////////////
	public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
		@Override
		public Result createFromParcel(Parcel in) {
			return new Result(in);
		}

		@Override
		public Result[] newArray(int size) {
			return new Result[size];
		}
	};
	
	public Result(Parcel in) {
		this.title = in.readString();
		this.scanTime = in.readLong();
	}
	
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(title);
		out.writeLong(scanTime);
	}

	@Override
	public int describeContents() {
		return 0;
	}
}
