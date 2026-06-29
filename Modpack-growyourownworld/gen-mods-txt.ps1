#requires -Version 5.1
<#
.SYNOPSIS
    Regenerate mods.txt from what's installed in overrides/ (mods, shaderpacks, resourcepacks).
.DESCRIPTION
    Lists the .jar files in overrides/mods and the .zip packs in overrides/shaderpacks and
    overrides/resourcepacks, grouped under section headers, so mods.txt always mirrors what's
    actually in the pack. Runtime-generated artifacts (e.g. the EuphoriaPatcher output folder
    and its .txt marker) are skipped, since only the downloadable .zip needs recording.
    Writes UTF-8 (no BOM), LF line endings.
.PARAMETER DryRun
    Show what would be written (and the diff vs the current mods.txt) without saving.
.EXAMPLE
    ./gen-mods-txt.ps1
.EXAMPLE
    ./gen-mods-txt.ps1 -DryRun
#>
[CmdletBinding()]
param(
    [string]$OverridesDir = (Join-Path $PSScriptRoot 'overrides'),
    [string]$OutFile      = (Join-Path $PSScriptRoot 'mods.txt'),
    [switch]$DryRun
)
$ErrorActionPreference = 'Stop'

# section name => subfolder + which files to list (only downloadable artifacts)
$sections = [ordered]@{
    'mods'          = @{ dir = 'mods';          filter = '*.jar' }
    'shaderpacks'   = @{ dir = 'shaderpacks';   filter = '*.zip' }
    'resourcepacks' = @{ dir = 'resourcepacks'; filter = '*.zip' }
}

$lines   = New-Object System.Collections.Generic.List[string]
$entries = New-Object System.Collections.Generic.List[string]   # flat list, for the diff
$total   = 0

foreach ($name in $sections.Keys) {
    $dir = Join-Path $OverridesDir $sections[$name].dir
    $files = @()
    if (Test-Path $dir) {
        $files = @(Get-ChildItem -LiteralPath $dir -Filter $sections[$name].filter -File |
            Sort-Object Name | Select-Object -ExpandProperty Name)
    }
    if ($lines.Count -gt 0) { $lines.Add('') }          # blank line between sections
    $lines.Add("# $name ($($files.Count))")
    foreach ($f in $files) { $lines.Add($f); $entries.Add($f) }
    $total += $files.Count
}

# diff vs current mods.txt (ignore section headers + blank lines)
$old = @()
if (Test-Path $OutFile) {
    $old = @(Get-Content -LiteralPath $OutFile |
        ForEach-Object { $_.Trim() } |
        Where-Object { $_ -and -not $_.StartsWith('#') })
}
$added   = @($entries | Where-Object { $old     -notcontains $_ })
$removed = @($old     | Where-Object { $entries -notcontains $_ })

Write-Host "$total entr$(if ($total -eq 1) {'y'} else {'ies'}) across mods / shaderpacks / resourcepacks" -ForegroundColor Cyan
foreach ($a in $added)   { Write-Host "  + $a" -ForegroundColor Green }
foreach ($r in $removed) { Write-Host "  - $r" -ForegroundColor Yellow }
if (-not $added -and -not $removed) { Write-Host '  (no change vs current mods.txt)' -ForegroundColor DarkGray }

if ($DryRun) { Write-Host 'DryRun - mods.txt not written.' -ForegroundColor DarkGray; return }

[IO.File]::WriteAllText($OutFile, (($lines -join "`n") + "`n"), (New-Object Text.UTF8Encoding($false)))
Write-Host "Wrote $OutFile" -ForegroundColor Green
