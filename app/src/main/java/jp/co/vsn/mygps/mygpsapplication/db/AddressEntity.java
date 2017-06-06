package jp.co.vsn.mygps.mygpsapplication.db;

public class AddressEntity {
    private int mId;
    private String mState;
    private boolean isComp;

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getState() {
        return mState;
    }

    public void setState(String mState) {
        this.mState = mState;
    }

    public boolean isComp() {
        return isComp;
    }

    public void setComp(int comp) {
        if (comp == 0) {
            this.isComp = false;
        } else {
            this.isComp = true;
        }
    }
}
