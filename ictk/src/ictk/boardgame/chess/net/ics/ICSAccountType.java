/*
 *  ICTK - Internet Chess ToolKit
 *  More information is available at http://ictk.sourceforge.net
 *  Copyright (C) 2002 J. Varsoke <jvarsoke@ghostmanonfirst.com>
 *  All rights reserved.
 *
 *  $Id$
 *
 *  This file is part of ICTK.
 *
 *  ICTK is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  ICTK is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ICTK; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package ictk.boardgame.chess.net.ics;

import java.util.regex.Matcher;
import java.util.StringTokenizer;
import java.io.IOException;

public class ICSAccountType {

   public final static int UNREGISTERED        = 0,
                           COMPUTER            = 1,
                           BLIND               = 2,
			   TEAM                = 3,
			   ADMIN	       = 4,
			   SERVICE_REP         = 5,
			   HELPER              = 6,
			   CHESS_ADVISOR       = 7,
			   TOURNAMENT_MANAGER  = 8,
			   TOURNAMENT_DIRECTOR = 9,
			   DISPLAY_MANAGER     = 10,
			   WFM		       = 11,
			   FM                  = 12,
			   WIM                 = 13,
			   IM                  = 14,
			   WGM                 = 15,
			   GM                  = 16,
			   NUM_FLAGS	       = 16;

   protected boolean[] flag;

   public ICSAccountType () {
      flag = new boolean[NUM_FLAGS];
   }

   public ICSAccountType (int mask) {
      this();
      set(mask);
   }

   public ICSAccountType (String s) throws IOException {
      this();
      if (s != null)
         set(s);
   }

   public void set (int type, boolean t) {
      flag[type] = t;
   }

   public boolean is (int type) {
      return flag[type];
   }

   public void set (String s) throws IOException {
      StringTokenizer st = new StringTokenizer(s, "()", false);
      String tok = null;

      while (st.hasMoreTokens()) {
         tok = st.nextToken();
	 if ("U".equals(tok)) flag[UNREGISTERED] = true;
	 else if ("*".equals(tok)) flag[ADMIN] = true;
	 else if ("C".equals(tok)) flag[COMPUTER] = true;
	 else if ("B".equals(tok)) flag[BLIND] = true;
	 else if ("SR".equals(tok)) flag[SERVICE_REP] = true;
	 else if ("H".equals(tok)) flag[HELPER] = true;
	 else if ("T".equals(tok)) flag[TEAM] = true;
	 else if ("TM".equals(tok)) flag[TOURNAMENT_MANAGER] = true;
	 else if ("TD".equals(tok)) flag[TOURNAMENT_DIRECTOR] = true;
	 else if ("FM".equals(tok)) flag[FM] = true;
	 else if ("IM".equals(tok)) flag[IM] = true;
	 else if ("WIM".equals(tok)) flag[WIM] = true;
	 else if ("WFM".equals(tok)) flag[WGM] = true;
	 else if ("WGM".equals(tok)) flag[WGM] = true;
	 else if ("GM".equals(tok)) flag[GM] = true;
	 else if ("CA".equals(tok)) flag[CHESS_ADVISOR] = true;
	 else if ("DM".equals(tok)) flag[DISPLAY_MANAGER] = true;
	 else 
	    throw new IOException ("Unrecognized account type: " + tok);
      }
   }

   public void set (int mask) {
      
   }

   public String typeToString (int type) {
      String s = null;
      
      switch (type) {
         case UNREGISTERED:        s = "U"; break;
	 case COMPUTER:            s = "C"; break;
	 case BLIND:		   s = "B"; break;
	 case TEAM:		   s = "T"; break;
	 case ADMIN:               s = "*"; break;
	 case TOURNAMENT_MANAGER:  s = "TM"; break;
	 case TOURNAMENT_DIRECTOR: s = "TD"; break;
	 case SERVICE_REP:         s = "SR"; break;
	 case HELPER:              s = "H"; break;
	 case CHESS_ADVISOR:       s = "CA"; break;
	 case DISPLAY_MANAGER:     s = "DM"; break;
	 case WFM:                 s = "WFM"; break;
	 case FM:                  s = "FM"; break;
	 case WIM:                 s = "WIM"; break;
	 case IM:                  s = "IM"; break;
	 case WGM:                 s = "WGM"; break;
	 case GM:                  s = "GM"; break;
	 default:
	    System.err.println("Received illegal account type: " + type);
      }
      return s;
   }

   public String toString () {
      StringBuffer sb = null;

      for (int i = 0; i < flag.length; i++) {
          if (flag[i]) {
	     if (sb == null)
	        sb = new StringBuffer();
	     sb.append("(").append(typeToString(i)).append(")");
	  }
            
      }
      if (sb == null) return "";
      else   return sb.toString();
   }
}
