package io.steve4744.papi.expansion.parkour;

import me.A5H73Y.Parkour.Course.Course;
import me.A5H73Y.Parkour.Course.CourseInfo;
import me.A5H73Y.Parkour.Course.CourseMethods;
import me.A5H73Y.Parkour.Other.TimeObject;
import me.A5H73Y.Parkour.Other.Validation;
import me.A5H73Y.Parkour.Parkour;
import me.A5H73Y.Parkour.Player.ParkourSession;
import me.A5H73Y.Parkour.Player.PlayerInfo;
import me.A5H73Y.Parkour.Player.PlayerMethods;
import me.A5H73Y.Parkour.Utilities.DatabaseMethods;
import me.A5H73Y.Parkour.Utilities.Utils;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkourExpansion extends PlaceholderExpansion implements Configurable {
    /**
     * This method should always return true unless we
     * have a dependency we need to make sure is on the server
     * for our placeholders to work!
     * This expansion does not require a dependency so we will always return true
     */
    @Override
    public boolean canRegister() {
        return true;
    }

    /**
     * Config options
     */
    @Override
    public Map<String, Object> getDefaults() {
        final Map<String, Object> defaults = new HashMap<>();
        defaults.put("lang.no-time-recorded", "No time recorded");
        defaults.put("lang.no-prize-delay", "0");
        return defaults;
    }

    /**
     * The name of the person who created this expansion should go here
     */
    @Override
    public String getAuthor() {
        return "steve4744";
    }

    /**
     * The placeholder identifier should go here
     * This is what tells PlaceholderAPI to call our onPlaceholderRequest method to obtain
     * a value if a placeholder starts with our identifier.
     * This must be unique and can not contain % or _
     */
    @Override
    public String getIdentifier() {
        return "parkour";
    }

    /**
     * if an expansion requires another plugin as a dependency, the proper name of the dependency should
     * go here. Set this to null if your placeholders do not require another plugin be installed on the server
     * for them to work
     *
     * @deprecated As of PlaceholderAPI 2.10.x
     */
    @Deprecated
    @Override
    public String getPlugin() {
        return "Parkour";
    }

    /**
     * This is the version of this expansion
     */
    @Override
    public String getVersion() {
        return "1.7";
    }

    /**
     * This is the method called when a placeholder with our identifier is found and needs a value
     * We specify the value identifier in this method
     */
    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) {
            return "";
        }
        if (identifier.equals("last_completed")) {
            String course = Parkour.getParkourConfig().getUsersData().getString("PlayerInfo." + p.getName() + ".LastCompleted");
            return course != null ? course : "";

        } else if (identifier.equals("last_played")) {
            String course = Parkour.getParkourConfig().getUsersData().getString("PlayerInfo." + p.getName() + ".LastPlayed");
            return course != null ? course : "";

        } else if (identifier.equals("level")) {
            return String.valueOf(PlayerInfo.getParkourLevel(p));

        } else if (identifier.equals("rank")) {
            String rank = PlayerInfo.getRank(p);
            return rank != null ? rank : "";

        } else if (identifier.equals("parkoins")) {
            return String.valueOf(PlayerInfo.getParkoins(p));

        } else if (identifier.equals("version")) {
            return Parkour.getPlugin().getDescription().getVersion();

        } else if (identifier.equals("player_count")) {
            return String.valueOf(PlayerMethods.getPlaying().size());

        } else if (identifier.equals("courses_completed")) {
            return PlayerInfo.getNumberOfCoursesCompleted(p);

        } else if (identifier.equals("course_count")) {
            return String.valueOf(CourseInfo.getAllCourses().size());

        } else if (identifier.equals("current_course")) {
            Course course = CourseMethods.findByPlayer(p.getName());
            return course != null ? course.getName() : "";

        } else if (identifier.equals("current_course_record_deaths")) {
            Course course = CourseMethods.findByPlayer(p.getName());
            if (course != null) {
                List<TimeObject> time = DatabaseMethods.getTopCourseResults(course.getName(), 1);
                if (time.isEmpty()) {
                    return getString("lang.no-time-recorded", "No time recorded");
                }
                return String.valueOf(time.get(0).getDeaths());
            }
            return "";

        } else if (identifier.equals("current_course_record")) {
            Course course = CourseMethods.findByPlayer(p.getName());
            if (course != null) {
                List<TimeObject> time = DatabaseMethods.getTopCourseResults(course.getName(), 1);
                if (time.isEmpty()) {
                    return getString("lang.no-time-recorded", "No time recorded");
                }
                return Utils.displayCurrentTime(time.get(0).getTime());
            }
            return "";

        } else if (identifier.startsWith("course_record_deaths")) {
            String[] temp = identifier.split("_");
            if (temp.length != 4) {
                return null;
            }
            String courseName = temp[3];
            if (!CourseMethods.exist(courseName)) {
                return null;
            }
            List<TimeObject> time = DatabaseMethods.getTopCourseResults(courseName, 1);
            if (time.isEmpty()) {
                return getString("lang.no-time-recorded", "No time recorded");
            }
            return String.valueOf(time.get(0).getDeaths());

        } else if (identifier.startsWith("course_record")) {
            String[] temp = identifier.split("_");
            if (temp.length != 3) {
                return null;
            }
            String courseName = temp[2];
            if (!CourseMethods.exist(courseName)) {
                return null;
            }
            List<TimeObject> time = DatabaseMethods.getTopCourseResults(courseName, 1);
            if (time.isEmpty()) {
                return getString("lang.no-time-recorded", "No time recorded");
            }
            return Utils.displayCurrentTime(time.get(0).getTime());

        } else if (identifier.startsWith("personal_best_deaths")) {
            String[] temp = identifier.split("_");
            if (temp.length != 4) {
                return null;
            }
            String courseName = temp[3];
            if (!CourseMethods.exist(courseName)) {
                return null;
            }
            List<TimeObject> time = DatabaseMethods.getTopPlayerCourseResults(p.getName(), courseName, 1);
            if (time.isEmpty()) {
                return getString("lang.no-time-recorded", "No time recorded");
            }
            return String.valueOf(time.get(0).getDeaths());

        } else if (identifier.startsWith("personal_best")) {
            String[] temp = identifier.split("_");
            if (temp.length != 3) {
                return null;
            }
            String courseName = temp[2];
            if (!CourseMethods.exist(courseName)) {
                return null;
            }
            List<TimeObject> time = DatabaseMethods.getTopPlayerCourseResults(p.getName(), courseName, 1);
            if (time.isEmpty()) {
                return getString("lang.no-time-recorded", "No time recorded");
            }
            return Utils.displayCurrentTime(time.get(0).getTime());

        } else if (identifier.equals("current_personal_best_deaths")) {
            Course course = CourseMethods.findByPlayer(p.getName());
            if (course != null) {
                List<TimeObject> time = DatabaseMethods.getTopPlayerCourseResults(p.getName(), course.getName(), 1);
                if (time.isEmpty()) {
                    return getString("lang.no-time-recorded", "No time recorded");
                }
                return String.valueOf(time.get(0).getDeaths());
            }
            return "";

        } else if (identifier.equals("current_personal_best")) {
            Course course = CourseMethods.findByPlayer(p.getName());
            if (course != null) {
                List<TimeObject> time = DatabaseMethods.getTopPlayerCourseResults(p.getName(), course.getName(), 1);
                if (time.isEmpty()) {
                    return getString("lang.no-time-recorded", "No time recorded");
                }
                return Utils.displayCurrentTime(time.get(0).getTime());
            }
            return "";

        } else if (identifier.startsWith("leader")) {
            String[] temp = identifier.split("_");
            if (temp.length != 2) {
                return null;
            }
            String courseName = temp[1];
            if (!CourseMethods.exist(courseName)) {
                return null;
            }
            List<TimeObject> time = DatabaseMethods.getTopCourseResults(courseName, 1);
            if (time.isEmpty()) {
                return getString("lang.no-time-recorded", "No time recorded");
            }
            return String.valueOf(time.get(0).getPlayer());

        } else if (identifier.equals("current_course_leader")) {
            Course course = CourseMethods.findByPlayer(p.getName());
            if (course != null) {
                List<TimeObject> time = DatabaseMethods.getTopCourseResults(course.getName(), 1);
                if (time.isEmpty()) {
                    return getString("lang.no-time-recorded", "No time recorded");
                }
                return String.valueOf(time.get(0).getPlayer());
            }
            return "";

        } else if (identifier.equals("current_course_timer")) {
            ParkourSession session = PlayerMethods.getParkourSession(p.getName());
            return session == null ? "" : session.getLiveTime();

        } else if (identifier.equals("current_course_deaths")) {
            ParkourSession session = PlayerMethods.getParkourSession(p.getName());
            return session == null ? "" : String.valueOf(session.getDeaths());

        } else if (identifier.startsWith("topten")) {
            String[] temp = identifier.split("_");
            if (temp.length != 3) {
                return null;
            }
            String courseName = temp[1];
            if (!CourseMethods.exist(courseName)) {
                return null;
            }
            if (!Validation.isInteger(temp[2])) {
                return null;
            }
            int pos = Integer.parseInt(temp[2]);
            if (pos < 1 || pos > 10) {
                return null;
            }
            List<TimeObject> time = DatabaseMethods.getTopCourseResults(courseName, pos);
            if (time.isEmpty()) {
                return getString("lang.no-time-recorded", "No time recorded");
            } else if (pos > time.size()) {
                return " ";
            }
            if (identifier.startsWith("toptenx")) {
                String nCol = "&f";
                String tCol = "&f";
                //check if colour codes specified
                if (temp[0].length() == 9 && temp[0].substring(7).matches("[0-9a-f]+")) {
                    nCol = "&" + temp[0].substring(7, 8);
                    tCol = "&" + temp[0].substring(8);
                }
                return nCol + time.get(pos - 1).getPlayer() + "&7 - " + tCol + Utils.displayCurrentTime(time.get(pos - 1).getTime());
            }
            return "&f" + pos + ") &b" + time.get(pos - 1).getPlayer() + "&f in &a" + Utils.displayCurrentTime(time.get(pos - 1).getTime()) + "&f";

        } else if (identifier.startsWith("course_prize_delay")) {
            String[] temp = identifier.split("_");
            if (temp.length != 4) {
                return null;
            }
            if (!CourseInfo.hasRewardDelay(temp[3]) || Utils.hasPrizeCooldownDurationPassed(p, temp[3], false)) {
                return getString("lang.no-prize-delay", "0");
            }
            return Utils.getTimeRemaining(p, temp[3]);

        } else if (identifier.startsWith("course_global_completions")) {
            String[] temp = identifier.split("_");
            if (temp.length != 4) {
                return null;
            }
            String courseName = temp[3];
            if (!CourseMethods.exist(courseName)) {
                return null;
            }
            return String.valueOf(Parkour.getParkourConfig().getCourseData().getInt(courseName + ".Completed", 0));

        }
        return null;
    }
}
