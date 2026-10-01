@file:Suppress("FunctionNaming", "MagicNumber")

package org.fossify.documents.ui.screens

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.fossify.commons.compose.theme.SimpleTheme
import org.fossify.commons.extensions.formatSize
import org.fossify.documents.R
import org.fossify.documents.models.DocumentEntry
import org.fossify.documents.models.DocumentFilter
import org.fossify.documents.models.DocumentKind

@Composable
internal fun FolderIcon() {
    Surface(
        modifier = Modifier.size(48.dp),
        shape = RoundedCornerShape(8.dp),
        color = SimpleTheme.colorScheme.primary.copy(alpha = primaryTintAlpha()),
        contentColor = SimpleTheme.colorScheme.primary,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = Icons.Filled.Folder,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
            )
        }
    }
}

@Composable
internal fun DocumentEntry.metaLine(showOpenedFallback: Boolean): String {
    val context = LocalContext.current
    val dateText = when {
        lastModified != null && lastModified > 0L -> DateUtils.formatDateTime(
            context,
            lastModified,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_ABBREV_MONTH or DateUtils.FORMAT_SHOW_YEAR,
        )

        showOpenedFallback && lastOpened > 0L -> stringResource(
            id = R.string.last_opened_value,
            DateUtils.getRelativeTimeSpanString(lastOpened).toString(),
        )

        else -> null
    }
    val sizeText = size?.formatSize()

    return listOfNotNull(dateText, sizeText).joinToString(" · ").ifBlank {
        kind.shortLabel()
    }
}

private fun DocumentKind.shortLabel(): String {
    return when (this) {
        DocumentKind.PDF -> "PDF"
        DocumentKind.DOCX -> "DOCX"
        DocumentKind.TEXT -> "TXT"
        DocumentKind.MARKDOWN -> "MD"
        DocumentKind.CSV -> "CSV"
        DocumentKind.HTML -> "HTML"
        DocumentKind.OTHER -> "DOC"
    }
}

@Composable
internal fun DocumentFilter.iconTint(): Color {
    val isDark = isDocumentsDarkTheme()
    return when (this) {
        DocumentFilter.PDF -> if (isDark) Color(0xFFFFDAD7) else Color(0xFFE52620)
        DocumentFilter.DOCX -> if (isDark) Color(0xFFD5E3FF) else Color(0xFF185ABD)
        DocumentFilter.CSV -> if (isDark) Color(0xFFB7E9C8) else Color(0xFF167044)
        DocumentFilter.HTML -> if (isDark) Color(0xFFFFDBCA) else Color(0xFFB84218)
        DocumentFilter.TEXT,
        DocumentFilter.MARKDOWN -> SimpleTheme.colorScheme.onSurface
    }
}
