package com.hit.changyou;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.solution.client.service.ServiceException;
import com.baidu.solution.pcs.sd.PcsSd;
import com.baidu.solution.pcs.sd.impl.ErrorInfo;
import com.baidu.solution.pcs.sd.impl.records.UpdateRecord;
import com.baidu.solution.pcs.sd.impl.tables.CreateTable;
import com.baidu.solution.pcs.sd.model.ColumnType;
import com.baidu.solution.pcs.sd.model.Order;
import com.baidu.solution.pcs.sd.model.Record;
import com.baidu.solution.pcs.sd.model.RecordSet;
import com.baidu.solution.pcs.sd.model.Table;
import com.baidu.solution.pcs.sd.model.condition.AndCondition;
import com.google.gson.Gson;
import com.hit.changyou.model.User;

public class PCS  {

	static final Logger LOGGER = Logger.getLogger(PCS.class.getName());

	/** Name of favorite POI table. */
	public static final String FAVORITE_TABLE = "favorite_POI";
	public static final String ID_INDEX = "id_index";
	/** Name of artist index. */
	public static final String NAME_INDEX = "name_index";

	/** Name of artist index. */
	public static final String TYPE_INDEX = "type_index";

	/** Get the access token of developer. */
	public static String getDeveloperAccessToken() {
		 return "access_token=3.e94d356cff26d08ff3d37abea3a45aa7.2592000.1380439103.2064424560-822726&sk=Bh2titcuKIL2aheqV3RWMq9QFr223bYa";
	}

	/** Get the access token of user. */
	public static String getUserAccessToken(User user) {
			return user.getAccess_token();
	}

	/**
	 * Create favorite poi table with the access token of developer(You).
	 * 
	 * @throws IOException
	 *             If error occurs while executing the request
	 * @throws ServiceException
	 *             (extends IOException) If the server of service returns error
	 *             code
	 */
	public static Table createFavoritePOITable()
			throws IOException {
		// MUST use the access token of developer. The users' access token has
		// no permission to do the request about table, only the records.
		PcsSd service = new PcsSd(getDeveloperAccessToken());
		// Add columns.
		CreateTable create = service.tables().create(FAVORITE_TABLE);
		create.addColumn("id", "id of POI", ColumnType.STRING, true);
		create.addColumn("name", "name of POI", ColumnType.STRING, true);
		create.addColumn("description", "description of POI", ColumnType.STRING, true);
		create.addColumn("type", "type name to which the POI belong",
				ColumnType.INT, true);
		create.addColumn("latitude", "latitude of POI", ColumnType.FLOAT, true);
		create.addColumn("longitude", "longitude of POI", ColumnType.FLOAT, true);
		// Add indexes.
		create.addIndex(ID_INDEX, "id", Order.ASC).addIndex(
				NAME_INDEX, "name", Order.ASC).addIndex(TYPE_INDEX, "type", Order.ASC);
		
		return create.execute();
	}

	/**
	 * @param userAccessToken
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static RecordSet insertRecords(POIBean bean,User user)
			throws IOException {
		// MUST use access token of user whose information will be inserted to
		// the table with the record.
		PcsSd service = new PcsSd(user.getAccess_token());

		// Create some favorite POIs and inserts them to favorite table.
		return service.records().insert(FAVORITE_TABLE, bean).execute();
	}

	/**
	 * @param userAccessToken
	 * @param name
	 * @param albums
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	public static RecordSet selectRecordsByPOIName(
			 String name, User user)
			throws IOException {
		return new PcsSd(user.getAccess_token())
				.records()
				.select(FAVORITE_TABLE)
				.setCondition(
						new AndCondition().addEqual(
								"name", name)).execute();
	}

	/**
	 * Correct wrong records.
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */
//	public static RecordSet correctRecords(String userAccessToken,
//			RecordSet records) throws IOException {
//		if (records.getRecords().size() < 1) {
//			return new RecordSet();
//		}
//
//		// Correct all records with wrong information.
//		UpdateRecord updateRecord = null;
//		for (Record wrongRecord : records.getRecords()) {
//			// Correct the record.
//			POI wrongPOI = wrongRecord.toType(POI.class);
//			POI correctPOI = wrongPOI.setAlbums("Super Star");
//			// Add first update to update record request.
//			if (null == updateRecord) {
//				updateRecord = new PcsSd(getUserAccessToken()).records()
//						.update(FAVORITE_TABLE, wrongRecord.getKey(),
//								wrongRecord.getModifyTime(), correctPOI);
//				continue;
//			}
//			// Add others.
//			updateRecord.add(wrongRecord.getKey(), wrongRecord.getModifyTime(),
//					wrongRecord);
//		}
//
//		// Update the records with the wrong albums information.
//		return updateRecord.setReplace().execute();
//	}

	Table table;

	RecordSet records;

	
	
}
