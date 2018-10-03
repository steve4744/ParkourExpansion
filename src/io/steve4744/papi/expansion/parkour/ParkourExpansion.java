package io.steve4744.papi.expansion.parkour;

import java.util.List;

import org.bukkit.entity.Player;

import me.A5H73Y.Parkour.Parkour;
import me.A5H73Y.Parkour.Course.Course;
import me.A5H73Y.Parkour.Course.CourseInfo;
import me.A5H73Y.Parkour.Course.CourseMethods;
import me.A5H73Y.Parkour.Other.TimeObject;
import me.A5H73Y.Parkour.Player.PlayerInfo;
import me.A5H73Y.Parkour.Player.PlayerMethods;
import me.A5H73Y.Parkour.Utilities.DatabaseMethods;
import me.A5H73Y.Parkour.Utilities.Utils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class ParkourExpansion extends PlaceholderExpansion {
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
     */
    @Override
    public String getPlugin() {
        return "Parkour";
    }

    /**
     * This is the version of this expansion
     */
    @Override
    public String getVersion() {
        return "1.0.8";
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
        	return Parkour.getParkourConfig().getUsersData().getString("PlayerInfo." + p.getName() +".LastCompleted");
        	
        } else if (identifier.equals("last_played")) {
        	return Parkour.getParkourConfig().getUsersData().getString("PlayerInfo." + p.getName() +".LastPlayed");
        	
        } else if (identifier.equals("level")) {
            return String.valueOf(Parkour.getParkourConfig().getUsersData().getInt("PlayerInfo." + p.getName() +".Level"));
            
        } else if (identifier.equals("rank")) {
            return Parkour.getParkourConfig().getUsersData().getString("PlayerInfo." + p.getName() +".Rank");
        
        } else if (identifier.equals("parkoins")) {
        	return String.valueOf(PlayerInfo.getParkoins(p));
        	
        } else if (identifier.equals("version")) {
        	return String.valueOf(Parkour.getPlugin().getDescription().getVersion());
        	
        } else if (identifier.equals("player_count")) {
        	return String.valueOf(PlayerMethods.getPlaying().size());
        	
        } else if (identifier.equals("course_count")) {
        	return String.valueOf(CourseInfo.getAllCourses().size());
        	
        } else if (identifier.equals("current_course")) {
        	Course course = CourseMethods.findByPlayer(p.getName());
        	return course != null ? course.getName() : null;
        
        } else if (identifier.equals("current_course_record")) {
        	Course course = CourseMethods.findByPlayer(p.getName());
        	if (course != null) {
        		List<TimeObject> time = DatabaseMethods.getTopCourseResults(course.getName(), 1);
            	if (time.size() == 0) {
            		return "No time recorded";
            	}
            	return Utils.displayCurrentTime(time.get(0).getTime());
        	}
        	return null;
        
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
        	if (time.size() == 0) {
        		return "No time recorded";
        	}
        	return Utils.displayCurrentTime(time.get(0).getTime());
        	
        } else if (identifier.startsWith("personal_best")) {
        	String[] temp = identifier.split("_");          
            if (temp.length != 3) {
              return null;
            }
            String courseName = temp[2];
            if (!CourseMethods.exist(courseName)) {
    			return null;
            }
        	List<TimeObject> time = DatabaseMethods.getTopPlayerCourseResults(p.getName(),courseName, 1);
        	if (time.size() == 0) {
        		return "No time recorded";
        	}
        	return Utils.displayCurrentTime(time.get(0).getTime());
        	
        } else if (identifier.equals("current_personal_best")) {
        	Course course = CourseMethods.findByPlayer(p.getName());
        	if (course != null) {
        		List<TimeObject> time = DatabaseMethods.getTopPlayerCourseResults(p.getName(),course.getName(), 1);
            	if (time.size() == 0) {
            		return "No time recorded";
            	}
            	return Utils.displayCurrentTime(time.get(0).getTime());
        	}
        	return null;
        
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
        	if (time.size() == 0) {
        		return "No time recorded";
        	}
        	return String.valueOf(time.get(0).getPlayer());
        
        } else if (identifier.equals("current_course_leader")) {
        	Course course = CourseMethods.findByPlayer(p.getName());
        	if (course != null) {
        		List<TimeObject> time = DatabaseMethods.getTopCourseResults(course.getName(), 1);
            	if (time.size() == 0) {
            		return "No time recorded";
            	}
            	return String.valueOf(time.get(0).getPlayer());
        	}
        	return null;
        	
        } else if (identifier.equals("current_course_timer")) {
        	if (PlayerMethods.getParkourSession(p.getName()) != null) {
        		return PlayerMethods.getParkourSession(p.getName()).getLiveTime();
        	}
        	return null;
        	
        } else if (identifier.startsWith("topten")) {
        	String[] temp = identifier.split("_");          
            if (temp.length != 3) {
              return null;
            }
            String courseName = temp[1];
            if (!CourseMethods.exist(courseName)) {
    			return null;
            }
            if (!Utils.isNumber(temp[2])) {
            	return null;
            }
            int pos = Integer.parseInt(temp[2]);
            if (pos < 1 || pos > 10) {
            	return null;
            }
            List<TimeObject> time = DatabaseMethods.getTopCourseResults(courseName, pos);
        	if (time.size() == 0) {
        		return "No time recorded";
        	} else if (pos > time.size()) {
        		return " ";		
        	}
        	if (identifier.startsWith("toptenx")){
        		String nCol = "&f";
        		String tCol = "&f";
        		//check if colour codes specified
        		if (temp[0].length() == 9 && temp[0].substring(7).matches("[0-9a-f]+")) {
        			nCol = "&" + temp[0].substring(7,8);
        			tCol = "&" + temp[0].substring(8);
        		}
        		return String.valueOf(nCol + time.get(pos - 1).getPlayer() + "&7 - " + tCol + Utils.displayCurrentTime(time.get(pos - 1).getTime()));
        	}
        	return String.valueOf("&f" + pos + ") &b" +time.get(pos - 1).getPlayer() + "&f in &a" + Utils.displayCurrentTime(time.get(pos - 1).getTime()) + "&f");
        }    
        return null;
    }
}
