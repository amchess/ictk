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

package ictk.boardgame.chess.net.ics.event;
import ictk.boardgame.chess.net.ics.*;

public class ICSBoardUpdateEvent extends ICSEvent 
                                          implements ICSBoardEvent {
   public static final int EVENT_TYPE = ICSEvent.BOARD_UPDATE_EVENT;
   public static final int ISO_POSITION_RELATION = -3,
                           OBSERVING_EXAMINED_RELATION = -2,
			   EXAMINING_RELATION = 2,
			   PLAYING_NOT_MY_MOVE_RELATION = -1,
			   PLAYING_MY_MOVE_RELATION = 1,
			   OBSERVING_PLAY_RELATION = 0;
                           
   String white, black, verboseMove, sanMove;
   protected boolean isBlackMove,
                     canWhiteCastleOO,
		     canWhiteCastleOOO,
		     canBlackCastleOO,
		     canBlackCastleOOO,
		     flipBoard,
		     clockMoving;
   protected char[][] board;
   protected int enpassantFile = -1,
                    /** 50 move rule count */
                 irreversable,  
                 boardNumber,
		 moveNumber,
/*** my relation to this game:
    -3 isolated position, such as for "ref 3" or the "sposition" command
    -2 I am observing game being examined
     2 I am the examiner of this game
    -1 I am playing, it is my opponent's move
     1 I am playing and it is my move
     0 I am observing a game being played
*/
		 relation,
		 /**initial time control in seconds*/
		 itime, 
		 /**increment in seconds*/
		 incr,
		 /**white's total material*/
		 whiteMaterial,
		 /**black's total material*/
		 blackMaterial,
		 whiteClock,
		 blackClock,
		 /**time spend on the last move in seconds**/
		 moveTime,
		 /**timeseal delta (lag)*/
		 timesealDelta;


   //getters setters//////////////////////////////////////////////////////////
   public ICSBoardUpdateEvent (ICSProtocolHandler server, int eventType) {
      super(server, eventType);
      board = new char[8][8];
   }

   public ICSBoardUpdateEvent (ICSProtocolHandler server) {
      this(server, BOARD_UPDATE_EVENT);
   }

   public char[][] getBoardArray () { return board; }

   public boolean isBlackToMove () { return isBlackMove; }

   public String getWhitePlayer () { return white; }
   public String getBlackPlayer () { return black; }
   public int getMoveNumber () { return moveNumber; }
   public int getEnPassantFileIndex () { return enpassantFile; }
   public int getPlySinceLastIrreversableMove () { return irreversable; }
   public int getBoardNumber () { return boardNumber; } 
   public int getRelation () { return relation; }
   public int getInitialTime () { return itime; }
   public int getIncrement () { return incr; }
   public int getWhiteMaterial () { return whiteMaterial; }
   public int getBlackMaterial () { return blackMaterial; }
   public int getTimeOnWhiteClock () { return whiteClock; }
   public int getTimeOnBlackClock () { return blackClock; }
   public int getLag () { return timesealDelta; }
   public String getSAN () { return sanMove; }
   public boolean isBlackMove () { return isBlackMove; }

   public int getMoveTime () { return moveTime; }

   public String getMoveTimeAsString () { 
      return getClockAsString(moveTime, true); 
   }

   public void setBoardNumber (int board) { boardNumber = board; }

   public String getBlackClockAsString () { 
      return getClockAsString(blackClock, false);
   }

   public String getWhiteClockAsString () { 
      return getClockAsString(whiteClock, false);
   }

   protected String getClockAsString (int clock, boolean move) {
      StringBuffer sb = new StringBuffer(7);
      int h, m, s, ms;

      h = clock / 3600000;
      m = (clock % 3600000) / 60000;
      s = (clock % 60000) / 1000;
      ms = clock % 1000;

      if (move && h > 1) {
	 sb.append(h).append(":");
	 if (m < 10)
	    sb.append(0);
      }
      sb.append(m).append(":");
      if (s < 10)
         sb.append(0);
      sb.append(s).append(".");
      if (ms < 100)
         sb.append(0);
      if (ms < 10)
         sb.append(0);
      sb.append(ms);
      return sb.toString();
   }

   ////////////////////////////////////////////////////////////////////////
   public String getReadable () {
      StringBuffer sb = new StringBuffer(80);
      sb.append("Board Update(" + getBoardNumber() + "): ")
        .append(getWhitePlayer())
        .append(" vs. ")
        .append(getBlackPlayer())
        .append("\n\n");

         int r =board.length-1, f=0;
         for (r = board[0].length-1; r >= 0; r--) {
	    sb.append ("   ").append(r+1).append("  ");
            for (f=0; f < board.length; f++) {
               if (board[f][r] != ' ')
                  sb.append(board[f][r]);
               else
                  if ((f+r) % 2 == 1)
                     sb.append("#");
                  else
                     sb.append(" ");
               sb.append(" ");

            }
	    if (r == 5)
	       sb.append("  ")
	         .append(getBlackClockAsString());
	    if (r == 3)
	       sb.append("  ")
	         .append(getWhiteClockAsString());
            sb.append("\n");
        }
	sb.append("\n      A B C D E F G H\n\n");
	if (!isBlackMove())
	   sb.append("         ")
	     .append(getMoveNumber())
	     .append(".")
             .append(getSAN()).append("\n");
        else
	   sb.append("       ")
	     .append(getMoveNumber())
	     .append("..")
             .append(getSAN()).append("\n");

      return sb.toString();
   }

   public String toString () {
      return getReadable();
   }

}
