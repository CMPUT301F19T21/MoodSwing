package com.example.moodswing.customDataTypes;

public class Utc {
    private Long utcLong;


    public Utc(){}

    /**
     * UTC is Universal Coordinated Time, allows time zone overlap resolve
     * @param utcLong milliseconds since midnight Jan 1sr 1970
     */

    public Utc(Long utcLong) {
        this.utcLong = utcLong;
    }

    public Long getUtcLong() {
        return utcLong;
    }

    public void setUtcLong(Long utcLong) {
        this.utcLong = utcLong;
    }
}
